package com.verisure.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.verisure.backend.dto.response.SdgResponseDTO;
import com.verisure.backend.entity.Sdg;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.mapper.SdgMapper;
import com.verisure.backend.repository.SdgRepository;

@ExtendWith(MockitoExtension.class)
public class SdgServiceTest {

    @Mock
    private SdgRepository sdgRepository;

    @Mock
    private SdgMapper sdgMapper;

    @InjectMocks
    private SdgServiceImpl sdgService;

    @Test
    @DisplayName("Happy Path: Retornar ODS 7 correctamente mapeado")
    void getById_Success() {

        Integer id = 7;
        String expectedName = "Energía asequible y no contaminante";

        Sdg sdg = new Sdg();
        sdg.setId(id);
        sdg.setName(expectedName);

        SdgResponseDTO expectedDto = new SdgResponseDTO(id, expectedName);

        when(sdgRepository.findById(id)).thenReturn(Optional.of(sdg));
        when(sdgMapper.toSdgResponseDTO(sdg)).thenReturn(expectedDto);

        SdgResponseDTO result = sdgService.getById(id);

        assertNotNull(result, "El DTO devuelto no debería ser nulo");
        assertEquals(expectedName, result.name(), "El nombre del ODS no coincide");
        // assertEquals("Casi somos libres", result.name(), "El nombre del ODS no
        // coincide");
        assertEquals(id, result.id(), "El ID del ODS no coincide");

        verify(sdgRepository).findById(id);
        verify(sdgMapper).toSdgResponseDTO(sdg);
    }

    @Test
    @DisplayName("Sad Path: Lanzar ResourceNotFoundException cuando el ODS no existe")
    void getById_NotFound() {

        Integer id = 18;
        when(sdgRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            sdgService.getById(id);
        }, "Debería lanzar ResourceNotFoundException para un ID inexistente");

        // assertThrows(NullPointerException.class, () -> {
        //     sdgService.getById(id);
        // }, "Debería lanzar ResourceNotFoundException para un ID inexistente");

        verify(sdgMapper, never()).toSdgResponseDTO(any());
    }

}