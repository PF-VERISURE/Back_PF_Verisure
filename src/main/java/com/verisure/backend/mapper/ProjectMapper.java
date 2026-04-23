package com.verisure.backend.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;
import com.verisure.backend.entity.Project;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "gno", ignore = true)
    @Mapping(target = "sdgs", ignore = true)
    Project toEntity(ProjectRequestDTO dto);

    @Mapping(source = "gno.organizationName", target = "gnoName")
    @Mapping(target = "sdgs", expression = "java(project.getSdgs().stream().map(s -> s.getName()).toList())")
    ProjectResponseDTO toResponseDTO(Project project);

    List<ProjectResponseDTO> toResponseDTOList(List<Project> projects);

}