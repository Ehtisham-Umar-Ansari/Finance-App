package com.eadevs.finance_app.dto;

import com.eadevs.finance_app.model.Role;
import lombok.Data;

@Data
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Boolean active;
}