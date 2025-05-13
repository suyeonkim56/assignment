package com.example.assignment.common.exception.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //400
    USER_ROLE_NOT_FOUND(HttpStatus.BAD_REQUEST, "A002", "권한 이름을 잘못 입력하셨습니다.");

    //401

    //402

    //403

    //404

    private final HttpStatus status;
    private final String code;
    private final String message;

}
