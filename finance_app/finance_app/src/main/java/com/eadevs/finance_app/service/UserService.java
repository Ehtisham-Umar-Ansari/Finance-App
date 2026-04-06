package com.eadevs.finance_app.service;

import com.eadevs.finance_app.config.SecurityConfig;
import com.eadevs.finance_app.dto.UserRequestDTO;
import com.eadevs.finance_app.dto.UserResponseDTO;
import com.eadevs.finance_app.model.Role;
import com.eadevs.finance_app.model.User;
import com.eadevs.finance_app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final SecurityConfig securityConfig;

    public UserService(UserRepository userRepository, SecurityConfig securityConfig1) {
        this.userRepository = userRepository;
        this.securityConfig = securityConfig1;
    }

    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = toEntity(dto);
        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public UserResponseDTO toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // IMPROVEMENT: Simplified null check
        user.setActive(user.getActive() == null || !user.getActive());
        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return toResponseDTO(user);
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Only update fields that are provided (partial update support)
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(securityConfig.passwordEncoder().encode(dto.getPassword()));
        }
        if (dto.getRole() != null) {
            user.setRole(dto.getRole());
        }
        if (dto.getActive() != null) {
            user.setActive(dto.getActive());
        }

        User saved = userRepository.save(user);
        return toResponseDTO(saved);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<UserResponseDTO> getUsersByRole(Role role) {
        return userRepository.findByRole(role)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(securityConfig.passwordEncoder().encode(dto.getPassword()));
        user.setRole(dto.getRole());

        // Set active status with default value true if not provided
        user.setActive(dto.getActive() != null ? dto.getActive() : true);

        return user;
    }

    private UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setActive(user.getActive());
        return response;
    }
}
