package com.verisure.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectListResponseDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;

import com.verisure.backend.entity.GnoProfile;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.entity.enums.StatusProject;
import com.verisure.backend.entity.User;
import com.verisure.backend.exception.*;
import com.verisure.backend.mapper.ProjectMapper;

import com.verisure.backend.repository.GnoProfileRepository;
import com.verisure.backend.repository.ProjectRepository;
import com.verisure.backend.repository.SdgRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final GnoProfileRepository gnoProfileRepository;
    private final SdgRepository sdgRepository;
    private final ProjectMapper projectMapper;
    
    //-------Para ONG----/
    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO dto, Long userId){

        if (dto.endDate().isBefore(dto.startDate())) {
            throw new BadRequestException("La fecha de fin no puede ser anterior a la de inicio");
        }

        Project project = projectMapper.toEntity(dto);

        GnoProfile gnoProfile = gnoProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Este usuario no tiene un perfil de ONG configurado"));

        project.setGno(gnoProfile);
        
        if (dto.sdgIds() != null && !dto.sdgIds().isEmpty()) {
            List<Sdg> sdgs = sdgRepository.findAllById(dto.sdgIds());
            if (sdgs.size() != dto.sdgIds().size()) {
             throw new ResourceNotFoundException("Uno o más SDGs no fueron encontrados");
            }
            project.setSdgs(sdgs);
        }
        
        Project saved = projectRepository.save(project);
        return projectMapper.toResponseDTO(saved);
    }

    @Override
    public ProjectResponseDTO updateProject(ProjectRequestDTO dto, Long id, Long userId) {
        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));

        GnoProfile currentGno = gnoProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de ONG no encontrado"));

        if (!project.getGno().getId().equals(currentGno.getId())) {
            throw new BadRequestException("No tienes permisos para editar este proyecto");
        }

        if (dto.endDate().isBefore(dto.startDate())) {
            throw new BadRequestException("La fecha de fin no puede ser anterior a la de inicio");
        }

        project.setTitle(dto.title());
        project.setDescription(dto.description());
        project.setImageUrl(dto.imageUrl());
        project.setRequiredVolunteers(dto.requiredVolunteers());
        project.setLocationType(dto.locationType());
        project.setImpactUnit(dto.impactUnit());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        project.setTotalHours(dto.totalHours());

        if (dto.sdgIds() != null) {
            List<Sdg> sdgs = sdgRepository.findAllById(dto.sdgIds());
            if (sdgs.size() != dto.sdgIds().size()) {
            throw new ResourceNotFoundException("Uno o más SDGs no fueron encontrados");
            }
            project.setSdgs(sdgs);
        }

        return projectMapper.toResponseDTO(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long id, Long userId) {

        Project project = projectRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));

        GnoProfile currentGno = gnoProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de ONG no encontrado"));

        // Validar que sea su propio proyecto
        if (!project.getGno().getId().equals(currentGno.getId())) {
            throw new BadRequestException("No tienes permisos para eliminar este proyecto");
        }

        projectRepository.delete(project);
    }

    @Override
    public List<ProjectResponseDTO> getMyProjects(Long userId) {
        GnoProfile gno = gnoProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene perfil de ONG"));
        
        return projectRepository.findByGnoId(gno.getId()).stream()
            .map(projectMapper::toResponseDTO)
            .toList();
    }

    // @Override // Refactor Alex feedback para getMyProjects
    // public ProjectListResponseDTO getMyProjects(Long userId) {
    //     GnoProfile gno = gnoProfileRepository.findByUserId(userId)
    //             .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene perfil de ONG"));
    //     List<ProjectResponseDTO> projectsList = projectRepository.findByGnoId(gno.getId()).stream()
    //         .map(projectMapper::toResponseDTO)
    //         .toList();
    //     return new ProjectListResponseDTO(projectsList, projectsList.size());
    // }

    //----Para Admin y Ong----/
    @Override
    public List<ProjectResponseDTO> getPendingProjects() {
        return projectRepository.findByStatus(StatusProject.PENDING)
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
    }

    // @Override // Refactor Alex feedback para getPendingProjects
    // public ProjectListResponseDTO getPendingProjects() {
    //     List<ProjectResponseDTO> pendingList = projectRepository.findByStatus(StatusProject.PENDING)
    //             .stream()
    //             .map(projectMapper::toResponseDTO)
    //             .toList();
    //     return new ProjectListResponseDTO(pendingList, pendingList.size());
    // }

    //---Admin
    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponseDTO> getAllProjectsForAdmin() {
        
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
            .map(projectMapper::toResponseDTO)
            .toList();
    }

    // @Override // Refactor Alex feedback para getAllProjectsForAdmin
    // @Transactional(readOnly = true)
    // public ProjectListResponseDTO getAllProjectsForAdmin() {
    //     List<ProjectResponseDTO> adminProjectsList = projectRepository.findAll().stream()
    //         .map(projectMapper::toResponseDTO)
    //         .toList();
    //     return new ProjectListResponseDTO(adminProjectsList, adminProjectsList.size());
    // }

}