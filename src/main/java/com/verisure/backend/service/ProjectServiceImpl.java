package com.verisure.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectListResponseDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;
import com.verisure.backend.entity.GnoProfile;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.entity.enums.StatusProject;
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
    private final CloudinaryService cloudinaryService;
    
    //-------Para ONG----/
    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO dto, Long userId, MultipartFile image){
        
        String imageUrl = (image != null && !image.isEmpty()) 
            ? cloudinaryService.uploadImage(image) 
            : null;
        
        if (dto.endDate().isBefore(dto.startDate())) {
            throw new BadRequestException("La fecha de fin no puede ser anterior a la de inicio");
        }    

        GnoProfile gnoProfile = gnoProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Este usuario no tiene un perfil de ONG configurado"));
        

        Project project = projectMapper.toEntity(dto);
        project.setGno(gnoProfile);
        project.setImageUrl(imageUrl);

        
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
    public ProjectResponseDTO updateProject(ProjectRequestDTO dto, Long id, Long userId, MultipartFile image) {
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

        if (image != null && !image.isEmpty()) {
            if (project.getImageUrl() != null) {
                cloudinaryService.deleteImage(extractPublicIdFromUrl(project.getImageUrl()));
            }
            String newImageUrl = cloudinaryService.uploadImage(image);
            project.setImageUrl(newImageUrl);
        }

        project.setTitle(dto.title());
        project.setDescription(dto.description());
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

        if (!project.getGno().getId().equals(currentGno.getId())) {
            throw new BadRequestException("No tienes permisos para eliminar este proyecto");
        }

        if (project.getImageUrl() != null){
            String publicId = extractPublicIdFromUrl(project.getImageUrl());
            cloudinaryService.deleteImage(publicId);
        }

        projectRepository.delete(project);
    }

    @Override
    public ProjectListResponseDTO getMyProjects(Long userId) {
        GnoProfile gno = gnoProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("El usuario no tiene perfil de ONG"));
        List<ProjectResponseDTO> projectsList = projectRepository.findByGnoId(gno.getId()).stream()
            .map(projectMapper::toResponseDTO)
            .toList();
        return new ProjectListResponseDTO(projectsList, projectsList.size());
    }


    //----Para Admin y Ong----/
    @Override
    public ProjectListResponseDTO getPendingProjects() {
        List<ProjectResponseDTO> pendingList = projectRepository.findByStatus(StatusProject.PENDING)
                .stream()
                .map(projectMapper::toResponseDTO)
                .toList();
        return new ProjectListResponseDTO(pendingList, pendingList.size());
    }


    //---Admin
    @Override
    @Transactional(readOnly = true)
    public ProjectListResponseDTO getAllProjectsForAdmin() {
        List<ProjectResponseDTO> adminProjectsList = projectRepository.findAll().stream()
            .map(projectMapper::toResponseDTO)
            .toList();
        return new ProjectListResponseDTO(adminProjectsList, adminProjectsList.size());
    }

    private String extractPublicIdFromUrl(String url) {
        if (url == null) return null;
        String publicId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        return "projects/" + publicId;
    }

}

