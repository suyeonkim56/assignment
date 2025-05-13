package com.example.assignment.common.enums;

import com.example.assignment.common.exception.constant.ErrorCode;
import com.example.assignment.common.exception.object.ClientException;

public enum Role {
    ADMIN,
    USER;

    public static Role of(String userRole)
    {
        for (Role role : values()) {
            if (role.name().equals(userRole)) {
                return role;
            }
        }
        throw new ClientException(ErrorCode.USER_ROLE_NOT_FOUND);
    }
}
