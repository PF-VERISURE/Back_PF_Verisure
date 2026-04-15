package com.verisure.backend.mapper;
import org.mapstruct.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import com.verisure.backend.dto.request.ProjectRequestDTO;
import com.verisure.backend.dto.response.ProjectResponseDTO;
import com.verisure.backend.entity.Project;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProjectMapper {
    
    //DTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    Project toEntity(ProjectRequestDTO dto);

    //Entity -> Response
    @Mapping(source = "gno.organizationName", target = "gnoName")
    @Mapping(target = "sdgs", expression = "java(project.getSgds().stream().map(s -> s.getName()).toList())")
    ProjectResponseDTO toResponseDTO(Project project);
}
