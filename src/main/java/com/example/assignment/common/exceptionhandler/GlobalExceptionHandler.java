package com.example.assignment.common.exceptionhandler;

import com.example.assignment.common.exception.constant.ErrorCode;
import com.example.assignment.common.exception.object.ClientException;
import com.example.assignment.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleClientException(ClientException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorResponse response = new ErrorResponse(
                errorCode.getStatus().value(),
                errorCode.getCode(),
                errorCode.getMessage()
        );
        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }
}
