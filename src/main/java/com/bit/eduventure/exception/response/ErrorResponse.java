package com.bit.eduventure.exception.response;

import com.bit.eduventure.exception.errorCode.ErrorCode;
import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
public class ErrorResponse {
    // HttpStatus
    private int status;

    // Http Default Message
    private String message;

    public ErrorResponse(ErrorCode errorCode){
        this.status = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
