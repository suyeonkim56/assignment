package com.example.assignment.auth.dto.response;

import com.example.assignment.auth.entity.User;
import lombok.Getter;

import java.util.List;

@Getter
public class GiveAdminResponseDto {
    private String username;
    private String nickname;
    private List<RoleDto> roles;

    public GiveAdminResponseDto(String username, String nickname, List<RoleDto> roles) {
        this.username = username;
        this.nickname = nickname;
        this.roles = roles;
    }

    public static GiveAdminResponseDto of(User user) {
        return new GiveAdminResponseDto(
                user.getUsername(),
                user.getNickname(),
                List.of(new RoleDto(user.getUserRole()))
        );
    }
}
