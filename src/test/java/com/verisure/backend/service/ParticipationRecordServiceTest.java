package com.verisure.backend.service;

import com.verisure.backend.entity.Application;
import com.verisure.backend.entity.ParticipationRecord;
import com.verisure.backend.entity.User;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.repository.ApplicationRepository;
import com.verisure.backend.repository.ParticipationRecordRepository;
import com.verisure.backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParticipationRecordServiceTest {

    @Mock
    private ParticipationRecordRepository participationRecordRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ParticipationRecordServiceImpl participationRecordService;

    @Test
    @DisplayName("Happy Path: Crea un nuevo registro de participación")
    void createParticipationRecord_Success() {
        
    }

    @Test
    @DisplayName("Sad Path: Lanza ResourceNotFoundException si la aplicación no existe")
    void createParticipationRecord_ApplicationNotFound() {
        
    }
}