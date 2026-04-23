package com.verisure.backend.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.dto.response.SdgResponseDTO;

@Mapper(componentModel = "spring")
public interface SdgMapper {

    SdgResponseDTO toSdgResponse(Sdg sdg);

    List<SdgResponseDTO> toSdgListResponse(List<Sdg> sdgs);

}