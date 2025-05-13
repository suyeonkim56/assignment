package com.example.assignment.common.enums;

import com.example.assignment.common.exception.constant.ErrorCode;
import com.example.assignment.common.exception.object.ClientException;

public enum UserRole {
    ADMIN,
    USER;

    public static UserRole of(String userRole)
    {
        for (UserRole role : values()) {
            if (role.name().equals(userRole)) {
                return role;
            }
        }
        throw new ClientException(ErrorCode.USER_ROLE_NOT_FOUND);
    }
}
