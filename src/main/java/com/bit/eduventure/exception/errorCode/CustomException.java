package com.bit.eduventure.exception.errorCode;

public class CustomException extends RuntimeException {
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
    public CustomException(String message) {
        super(message);
    }
}
