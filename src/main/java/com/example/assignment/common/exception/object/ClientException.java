package com.example.assignment.common.exception.object;

import com.example.assignment.common.exception.constant.ErrorCode;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException{
    private ErrorCode errorCode;

    public ClientException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
