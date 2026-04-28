package com.verisure.backend.service;

import com.verisure.backend.dto.response.CertificateResponseDTO;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.repository.ParticipationRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticipationRecordServiceTest {

    @Mock
    private ParticipationRecordRepository participationRecordRepository;

    @InjectMocks
    private ParticipationRecordServiceImpl participationRecordService;

    @Test
    @DisplayName("Happy Path: getMyCertificates retorna lista vacía si el usuario no tiene participaciones")
    void getMyCertificates_Empty() {

        Long userId = 2L;

        when(participationRecordRepository.findRawCertificatesByUserId(userId)).thenReturn(List.of());

        List<CertificateResponseDTO> result = participationRecordService.getMyCertificates(userId);

        assertTrue(result.isEmpty(), "La lista de certificados debería estar vacía");
        // assertFalse(result.isEmpty(), "Debería fallar, porque la lista SÍ está vacía");
        // assertEquals(1, result.size(), "Debería fallar, esperamos 0 elementos");

        verify(participationRecordRepository).findRawCertificatesByUserId(userId);
    }

    @Test
    @DisplayName("Sad Path: getCertificateById lanza excepción si el certificado no existe")
    void getCertificateById_NotFound() {

        Long participationId = 99L;
        Long userId = 2L;

        when(participationRecordRepository.findRawCertificateById(participationId, userId)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> {
            participationRecordService.getCertificateById(participationId, userId);
        }, "Debería lanzar ResourceNotFoundException para un certificado inexistente");

        // assertThrows(NullPointerException.class, () -> {
        //     participationRecordService.getCertificateById(participationId, userId);
        // }, "Debería fallar, porque lanza ResourceNotFoundException y no NullPointerException");

        verify(participationRecordRepository).findRawCertificateById(participationId, userId);
    }

}