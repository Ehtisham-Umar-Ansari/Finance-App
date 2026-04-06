package com.eadevs.finance_app.controller;

import com.eadevs.finance_app.dto.UserRequestDTO;
import com.eadevs.finance_app.dto.UserResponseDTO;
import com.eadevs.finance_app.model.Role;
import com.eadevs.finance_app.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO dto) {
        return userService.createUser(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    // IMPROVEMENT: Added update endpoint
    public UserResponseDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO dto) {
        return userService.updateUser(id, dto);
    }

    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO toggleUserStatus(@PathVariable Long id) {
        return userService.toggleUserStatus(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    // IMPROVEMENT: Added endpoint to filter users by role
    public List<UserResponseDTO> getUsersByRole(@PathVariable Role role) {
        return userService.getUsersByRole(role);
    }
}
