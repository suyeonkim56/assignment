package com.example.assignment.auth.controller;

import com.example.assignment.auth.dto.request.SignInRequestDto;
import com.example.assignment.auth.dto.request.SignUpRequestDto;
import com.example.assignment.auth.dto.response.GiveAdminResponseDto;
import com.example.assignment.auth.dto.response.SignInResponseDto;
import com.example.assignment.auth.dto.response.SignUpResponseDto;
import com.example.assignment.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(SignUpRequestDto dto) {
        return new ResponseEntity<>(authService.signUp(dto), HttpStatus.CREATED);
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<SignInResponseDto> signin(SignInRequestDto dto) {
        return new ResponseEntity<>(authService.signIn(dto), HttpStatus.OK);
    }

    //어드민 권한 부여
    @PatchMapping("/admin/users/{userId}/roles")
    public ResponseEntity<GiveAdminResponseDto> giveAdminRole(@PathVariable Long userId) {
        return new ResponseEntity<>(authService.giveAdminRole(userId), HttpStatus.OK);
    }
}
