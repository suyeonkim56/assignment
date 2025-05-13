package com.example.assignment.auth.dto.response;

import com.example.assignment.auth.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class SignUpResponseDto {
    private String username;
    private String nickname;
    private List<RoleDto> roles;

    public SignUpResponseDto(String username, String nickname, List<RoleDto> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }

    public static SignUpResponseDto of(User user) {
        return new SignUpResponseDto(
                user.getUsername(),
                user.getNickname(),
                List.of(new RoleDto(user.getUserRole()))
        );
    }
}

