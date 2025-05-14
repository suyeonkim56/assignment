package com.example.assignment.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignInRequestDto {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    public SignInRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
