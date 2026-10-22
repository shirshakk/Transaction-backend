package com.wallete.wallete_service.exception;

import com.wallete.wallete_service.enums.ErrorCode;

import lombok.Getter;

@Getter 
public class CustomException extends RuntimeException  {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
