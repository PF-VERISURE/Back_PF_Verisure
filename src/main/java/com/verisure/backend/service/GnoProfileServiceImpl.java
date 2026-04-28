package com.verisure.backend.service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.verisure.backend.dto.request.GnoCreateRequestDTO;
import com.verisure.backend.dto.response.GnoProfileListResponseDTO;
import com.verisure.backend.dto.response.GnoProfileResponseDTO;
import com.verisure.backend.entity.GnoProfile;
import com.verisure.backend.entity.User;
import com.verisure.backend.entity.enums.Role;
import com.verisure.backend.exception.DuplicateResourceException;
import com.verisure.backend.exception.ResourceNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import com.verisure.backend.mapper.GnoProfileMapper;
import com.verisure.backend.repository.GnoProfileRepository;
import com.verisure.backend.repository.UserRepository;

@Service
public class GnoProfileServiceImpl implements GnoProfileService {

    private final UserRepository userRepository;
    private final GnoProfileRepository gnoProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final GnoProfileMapper gnoProfileMapper;

    public GnoProfileServiceImpl(UserRepository userRepository, GnoProfileRepository gnoProfileRepository,
            PasswordEncoder passwordEncoder, GnoProfileMapper gnoProfileMapper) {
        this.userRepository = userRepository;
        this.gnoProfileRepository = gnoProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.gnoProfileMapper = gnoProfileMapper;
    }

    @Override
    @Transactional
    public GnoProfileResponseDTO createGnoProfile(GnoCreateRequestDTO request) {
        validateGnoRequest(request);
        User user = buildUserEntity(request);
        GnoProfile profile = gnoProfileMapper.toProfileEntity(request);
        linkUserWithProfile(user, profile);
        userRepository.save(user);
        return gnoProfileMapper.toResponseDTO(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public GnoProfileResponseDTO getMyProfile(Long userId) {
        GnoProfile profile = gnoProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Este usuario no tiene un perfil de ONG"));
        return gnoProfileMapper.toResponseDTO(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public GnoProfileResponseDTO getGnoProfile(Long id) {
        GnoProfile profile = gnoProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la ONG con el ID: " + id));
        return gnoProfileMapper.toResponseDTO(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public GnoProfileListResponseDTO getAllGnoProfiles() {
        List<GnoProfile> gnos = gnoProfileRepository.findAll();
        List<GnoProfileResponseDTO> gnoProfilesDTO = gnoProfileMapper.toResponseDTOList(gnos);
        return new GnoProfileListResponseDTO(gnoProfilesDTO, gnoProfilesDTO.size());
    }

    @Override
    @Transactional
    public void deleteGno(Long id) {
        GnoProfile profile = gnoProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la ONG con el ID: " + id));
        User user = profile.getUser();
        user.setDeletedAt(OffsetDateTime.now(ZoneOffset.UTC));
        userRepository.save(user);
    }

    
    private void validateGnoRequest(GnoCreateRequestDTO request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email: " + request.email());
        }
        if (gnoProfileRepository.existsByCif(request.cif())) {
            throw new DuplicateResourceException("Ya existe una ONG con el CIF: " + request.cif());
        }
    }

    private User buildUserEntity(GnoCreateRequestDTO request) {
        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Role.ONG);
        return user;
    }

    private void linkUserWithProfile(User user, GnoProfile profile) {
        profile.setUser(user);
        user.setGnoProfile(profile);
    }

}