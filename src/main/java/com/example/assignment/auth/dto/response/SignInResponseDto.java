package com.example.assignment.auth.dto.response;

import lombok.Getter;

@Getter
public class SignInResponseDto {
    private String token;

    public SignInResponseDto(String token) {
        this.token = token;
    }
}
