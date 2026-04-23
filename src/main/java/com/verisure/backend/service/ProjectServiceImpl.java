package com.verisure.backend.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.request.StatusUpdateRequestDTO;
import com.verisure.backend.dto.response.ProjectListResponseDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;
import com.verisure.backend.entity.GnoProfile;
import com.verisure.backend.entity.Project;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.entity.User;
import com.verisure.backend.entity.UserFavorite;
import com.verisure.backend.entity.enums.LocationType;
import com.verisure.backend.entity.enums.StatusApplication;
import com.verisure.backend.entity.enums.StatusProject;
import com.verisure.backend.exception.BadRequestException;
import com.verisure.backend.exception.ForbiddenException;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.mapper.ProjectMapper;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.GnoProfileRepository;
import com.verisure.backend.repository.ProjectRepository;
import com.verisure.backend.repository.SdgRepository;
import com.verisure.backend.repository.UserFavoriteRepository;
import com.verisure.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserFavoriteRepository userFavoriteRepository;
    private final ApplicationRepository applicationRepository;
    private final GnoProfileRepository gnoProfileRepository;
    private final SdgRepository sdgRepository;
    private final ProjectMapper projectMapper;
    private final CloudinaryService cloudinaryService;

    // -------Para ONG----/
    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO dto, Long userId, MultipartFile image) {
        validateProjectDates(dto.startDate(), dto.endDate());
        validateLocationType(dto);
        validateCity(dto);

        GnoProfile gnoProfile = getGnoProfileOrThrow(userId);
        List<Sdg> sdgs = getValidatedSdgs(dto.sdgIds());

        String imageUrl = (image != null && !image.isEmpty())
                ? cloudinaryService.uploadImage(image)
                : null;

        Project project = projectMapper.toEntity(dto);
        project.setGno(gnoProfile);
        project.setImageUrl(imageUrl);
        project.setSdgs(sdgs);

        Project saved = projectRepository.save(project);
        return projectMapper.toResponseDTO(saved, 0L, 0L);
    }

    @Override
    public ProjectResponseDTO updateProject(ProjectRequestDTO dto, Long id, Long userId, MultipartFile image) {
        validateProjectDates(dto.startDate(), dto.endDate());
        validateLocationType(dto);
        validateCity(dto);

        Project project = getProjectOrThrow(id);
        GnoProfile currentGno = getGnoProfileOrThrow(userId);

        if (!project.getGno().getId().equals(currentGno.getId())) {
            throw new BadRequestException("No tienes permisos para editar este proyecto");
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
        project.setAddress(dto.address());
        project.setCity(dto.city());
        project.setImpactUnit(dto.impactUnit());
        project.setStartDate(dto.startDate());
        project.setEndDate(dto.endDate());
        project.setTotalHours(dto.totalHours());

        project.setSdgs(getValidatedSdgs(dto.sdgIds()));

        Long favs = userFavoriteRepository.countByProjectId(project.getId());
        Long apps = applicationRepository.countProjectOccupancy(project.getId(), StatusApplication.APPROVED);

        return projectMapper.toResponseDTO(projectRepository.save(project), favs, apps);
    }

    @Override
    public void deleteProject(Long id, Long userId) {
        Project project = getProjectOrThrow(id);
        GnoProfile currentGno = getGnoProfileOrThrow(userId);

        if (!project.getGno().getId().equals(currentGno.getId())) {
            throw new ForbiddenException("No tienes permisos para editar este proyecto");
        }

        if (project.getImageUrl() != null) {
            String publicId = extractPublicIdFromUrl(project.getImageUrl());
            cloudinaryService.deleteImage(publicId);
        }

        projectRepository.delete(project);
    }

    @Override
    public ProjectListResponseDTO getMyProjects(Long userId) {
        GnoProfile currentGno = getGnoProfileOrThrow(userId);
        List<ProjectResponseDTO> projectsList = projectRepository.findByGnoId(currentGno.getId()).stream()
                .map(project -> {
                    long favs = userFavoriteRepository.countByProjectId(project.getId());
                    long apps = applicationRepository.countProjectOccupancy(project.getId(), StatusApplication.APPROVED);
                    return projectMapper.toResponseDTO(project, favs, apps);
                })
                .toList();
        return new ProjectListResponseDTO(projectsList, projectsList.size());
    }

    // ----Para Admin y Ong----/

    @Override
    public ProjectListResponseDTO getPendingProjects() {
        List<ProjectResponseDTO> pendingList = projectRepository.findByStatus(StatusProject.PENDING)
                .stream()
                .map(project -> {
                    long favs = userFavoriteRepository.countByProjectId(project.getId());
                    long apps = applicationRepository.countProjectOccupancy(project.getId(), StatusApplication.APPROVED);
                    return projectMapper.toResponseDTO(project, favs, apps);
                })
                .toList();
        return new ProjectListResponseDTO(pendingList, pendingList.size());
    }

    // ---Admin
    @Override
    @Transactional(readOnly = true)
    public ProjectListResponseDTO getAllProjectsForAdmin() {
        List<ProjectResponseDTO> adminProjectsList = projectRepository.findAll().stream()
                .map(project -> {
                    long favs = userFavoriteRepository.countByProjectId(project.getId());
                    long apps = applicationRepository.countProjectOccupancy(project.getId(), StatusApplication.APPROVED);
                    return projectMapper.toResponseDTO(project, favs, apps);
                })
                .toList();
        return new ProjectListResponseDTO(adminProjectsList, adminProjectsList.size());
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectListResponseDTO getAllPublished() {

        List<ProjectResponseDTO> publishedProjects = projectRepository.findByStatus(StatusProject.PUBLISHED)
                .stream()
                .map(project -> {
                    long favs = userFavoriteRepository.countByProjectId(project.getId());
                    long apps = applicationRepository.countProjectOccupancy(project.getId(), StatusApplication.APPROVED);
                    return projectMapper.toResponseDTO(project, favs, apps);
                })
                .toList();

        return new ProjectListResponseDTO(
            publishedProjects, 
            publishedProjects.size());
    }

    @Override
    public ProjectResponseDTO updateStatus(Long id, StatusUpdateRequestDTO statusDto) {
        Project project = getProjectOrThrow(id);
        project.setStatus(statusDto.status());
        Project updatedProject = projectRepository.save(project);
        long favs = userFavoriteRepository.countByProjectId(id);
        long apps = applicationRepository.countProjectOccupancy(id, StatusApplication.APPROVED);
        return projectMapper.toResponseDTO(updatedProject, favs, apps);
    }

    @Override
    public void toggleFavorite(Long id, Long userId) {
        Project project = getProjectOrThrow(id);
        User user =  userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        userFavoriteRepository.findByUserIdAndProjectId(userId, id)
            .ifPresentOrElse(
                favorite -> userFavoriteRepository.delete(favorite), // Si existe, lo borra (Dislike)
                () -> {
                    UserFavorite favorite = new UserFavorite(); // Si no existe, lo crea (Like)
                    favorite.setUser(user);
                    favorite.setProject(project);
                    userFavoriteRepository.save(favorite);
                }
            );
    }

    private String extractPublicIdFromUrl(String url) {
        if (url == null)
            return null;
        String publicId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("."));
        return "projects/" + publicId;
    }

    // metodos privados para DRY y validacion

    private GnoProfile getGnoProfileOrThrow(Long userId) {
        return gnoProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Este usuario no tiene un perfil de ONG configurado"));
    }

    private Project getProjectOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado con ID: " + id));
    }

    private List<Sdg> getValidatedSdgs(List<Integer> sdgIds) {
        if (sdgIds == null || sdgIds.isEmpty()) {
            return new ArrayList<>();
        }
        List<Sdg> sdgs = sdgRepository.findAllById(sdgIds);
        if (sdgs.size() != sdgIds.size()) {
            throw new ResourceNotFoundException("Uno o más SDGs no fueron encontrados");
        }
        return sdgs;
    }

    private void validateProjectDates(OffsetDateTime startDate, OffsetDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new BadRequestException("La fecha de fin no puede ser anterior a la de inicio");
        }
    }

    private void validateLocationType(ProjectRequestDTO dto) {
        if (dto.locationType() == LocationType.IN_PERSON || dto.locationType() == LocationType.HYBRID) {
            if (dto.address() == null || dto.address().trim().isEmpty()) {
                throw new BadRequestException("Para proyectos presenciales o híbridos, la dirección es obligatoria.");
            }
        } else if (dto.locationType() == LocationType.ONLINE) {
            if (dto.address() == null
                    || (!dto.address().startsWith("http://") && !dto.address().startsWith("https://"))) {
                throw new BadRequestException("El enlace del proyecto online debe comenzar por http:// o https://");
            }
        }
    }

    private void validateCity(ProjectRequestDTO dto) {
        if (dto.locationType() == LocationType.IN_PERSON || dto.locationType() == LocationType.HYBRID) {
            if (dto.city() == null || dto.city().trim().isEmpty()) {
                throw new BadRequestException("Para proyectos presenciales o híbridos, la ciudad es obligatoria.");
            }
        }
    }

}