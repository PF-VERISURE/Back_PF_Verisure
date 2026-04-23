package com.verisure.backend.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import com.verisure.backend.dto.request.EmployeeCreateRequestDTO;
import com.verisure.backend.dto.response.EmployeeProfileResponseDTO;
import com.verisure.backend.entity.EmployeeProfile;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeProfileMapper {

    @Mapping(source = "user.email", target = "email")
    EmployeeProfileResponseDTO toResponseDTO(EmployeeProfile employeeProfile);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    EmployeeProfile toProfileEntity(EmployeeCreateRequestDTO dto);

    List<EmployeeProfileResponseDTO> toResponseDTOList(List<EmployeeProfile> employeeProfiles);

}