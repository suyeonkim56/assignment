package com.example.assignment.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignUpRequestDto {
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    private String nickname;

    //테스트용
    public SignUpRequestDto(String username, String password, String nickname) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;

    }
}
