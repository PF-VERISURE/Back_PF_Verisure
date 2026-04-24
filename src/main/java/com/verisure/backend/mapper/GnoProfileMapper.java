package com.verisure.backend.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import com.verisure.backend.dto.request.GnoCreateRequestDTO;
import com.verisure.backend.dto.response.GnoProfileResponseDTO;
import com.verisure.backend.entity.GnoProfile;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GnoProfileMapper {

    @Mapping(source = "user.email", target = "email")
    GnoProfileResponseDTO toResponseDTO(GnoProfile gnoProfile);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    GnoProfile toProfileEntity(GnoCreateRequestDTO dto);

    List<GnoProfileResponseDTO> toResponseDTOList(List<GnoProfile> gnoProfiles);
    
}