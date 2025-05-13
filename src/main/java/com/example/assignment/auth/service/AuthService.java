package com.example.assignment.auth.service;

import com.example.assignment.auth.dto.request.SignInRequestDto;
import com.example.assignment.auth.dto.request.SignUpRequestDto;
import com.example.assignment.auth.dto.response.GiveAdminResponseDto;
import com.example.assignment.auth.dto.response.SignInResponseDto;
import com.example.assignment.auth.dto.response.SignUpResponseDto;
import com.example.assignment.auth.entity.User;
import com.example.assignment.auth.repository.AuthRepository;
import com.example.assignment.common.config.JwtUtil;
import com.example.assignment.common.config.PasswordEncoder;
import com.example.assignment.common.enums.UserRole;
import com.example.assignment.common.exception.constant.ErrorCode;
import com.example.assignment.common.exception.object.ClientException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    //회원가입
    public SignUpResponseDto signUp(SignUpRequestDto dto) {
        if (authRepository.existsByUsername(dto.getUsername())) {
            throw new ClientException(ErrorCode.USER_ALREADY_EXISTS);
        }

        User user = User.of(dto.getUsername(), passwordEncoder.encode(dto.getPassword()), dto.getNickname());

        authRepository.save(user);

        return SignUpResponseDto.of(user);
    }

    //로그인
    public SignInResponseDto signIn(SignInRequestDto dto) {
        User FindUser = authRepository.findByUsername(dto.getUsername()).orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(dto.getPassword(), FindUser.getPassword())) {
            throw new ClientException(ErrorCode.INVALID_PASSWORD);
        }
        String token = jwtUtil.createToken(FindUser.getId(), dto.getUsername());
        return new SignInResponseDto(token);
    }

    //어드민 권한 부여
    public GiveAdminResponseDto giveAdminRole(Long userId) {
        User FindUser = authRepository.findById(userId).orElseThrow(() -> new ClientException(ErrorCode.USER_NOT_FOUND));
        //이미 어드민이면 거절하기
        if (FindUser.getUserRole().equals(UserRole.ADMIN)) {
            throw new ClientException(ErrorCode.ACCESS_DENIED);
        }

        FindUser.setAdmin();
        return GiveAdminResponseDto.of(FindUser);
    }
}
