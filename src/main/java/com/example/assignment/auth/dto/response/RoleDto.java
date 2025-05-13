package com.example.assignment.auth.dto.response;

import com.example.assignment.common.enums.UserRole;
import lombok.Getter;

@Getter
public class RoleDto {
    private final String role;

    public RoleDto(UserRole userRole) {
        this.role = userRole.name();
    }
}

