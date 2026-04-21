package com.verisure.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.verisure.backend.dto.response.EmployeeProfileListResponseDTO;
import com.verisure.backend.dto.response.EmployeeProfileResponseDTO;
import com.verisure.backend.entity.EmployeeProfile;
import com.verisure.backend.exception.ResourceNotFoundException;
import com.verisure.backend.mapper.EmployeeProfileMapper;
import com.verisure.backend.repository.EmployeeProfileRepository;

@Service
public class EmployeeProfileServiceImpl implements EmployeeProfileService {

    private final EmployeeProfileRepository employeeProfileRepository;
    private final EmployeeProfileMapper employeeProfileMapper;

    public EmployeeProfileServiceImpl(EmployeeProfileRepository employeeProfileRepository,
            EmployeeProfileMapper employeeProfileMapper) {
        this.employeeProfileRepository = employeeProfileRepository;
        this.employeeProfileMapper = employeeProfileMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeProfileResponseDTO getMyProfile(Long userId) {
        EmployeeProfile profile = employeeProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Este usuario no tiene un perfil de Empleado"));
        return employeeProfileMapper.toResponseDTO(profile);
    }

    // para el admin poder ver todos, buscar uno por id. No Delete pq esto lo
    // gestiona RRHH.
    @Override
    @Transactional(readOnly = true)
    public EmployeeProfileListResponseDTO getAllEmployeeProfiles() {
        List<EmployeeProfileResponseDTO> employeeProfiles = employeeProfileRepository.findAll()
                .stream()
                .map(employeeProfileMapper::toResponseDTO)
                .toList();
        return new EmployeeProfileListResponseDTO(
                employeeProfiles,
                employeeProfiles.size()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeProfileResponseDTO getEmployeeProfile(Long id) {
        EmployeeProfile profile = employeeProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el empleado con el ID: " + id));
        return employeeProfileMapper.toResponseDTO(profile);
    }

}