package com.example.assignment.common.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // JWT 에러 코드

    // 401
    INVALID_JWT_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID_JWT_TOKEN", "유효하지 않은 JWT 토큰입니다."),

    // 404
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "TOKEN_NOT_FOUND", "토큰이 존재하지 않습니다."),

    // 회원 에러코드

    // 403
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다."),

    // 404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 회원입니다."),

    // 회원가입/로그인 에러 코드

    // 400
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "USER_ALREADY_EXISTS", "이미 가입된 사용자입니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "INVALID_PASSWORD", "비밀번호가 일치하지 않습니다."),
    USER_ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST,"USER_ROLE_NOT_FOUND","존재하지 않는 권한입니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;

}
