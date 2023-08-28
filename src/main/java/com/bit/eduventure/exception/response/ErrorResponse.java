package com.bit.eduventure.exception.response;

import com.google.gson.annotations.SerializedName;
import com.bit.eduventure.exception.errorCode.ErrorCode;
import lombok.Getter;


@Getter
public class ErrorResponse {
    @SerializedName("statusCode")
    private int code;

    @SerializedName("errorMessage")
    private String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    public ErrorResponse(String message) {
        this.message = message;
    }
}
