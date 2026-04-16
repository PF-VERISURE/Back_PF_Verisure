package com.verisure.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.verisure.backend.dto.request.ProjectRequestDTO;
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
import com.verisure.backend.repository.UserRepository;

//import com.verisure.backend.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;
    private final GnoProfileRepository gnoProfileRepository;
    private final SdgRepository sdgRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    
    @Override
    public ProjectResponseDTO createProject(ProjectRequestDTO dto, String email){

        if (dto.endDate().isBefore(dto.startDate())) {
            throw new BadRequestException("La fecha de fin no puede ser anterior a la de inicio");
        }

        Project project = projectMapper.toEntity(dto);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));

        GnoProfile gnoProfile = user.getGnoProfile();
        if (gnoProfile == null) {
            throw new ResourceNotFoundException("Este usuario no tiene un perfil de ONG configurado");
        }

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


}
