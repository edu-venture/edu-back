package com.bit.eduventure.exception.controller;


import com.bit.eduventure.exception.errorCode.ErrorCode;
import com.bit.eduventure.exception.response.ErrorResponse;
import com.google.gson.Gson;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler({
            Exception.class,
            NoSuchElementException.class,
            NullPointerException.class,
            ClassCastException.class,
            StackOverflowError.class,
            DataFormatException.class,
            RuntimeException.class
    })
    protected ResponseEntity<String> exceptionHandler(Exception e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.EXCEPTION);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> setResponse(ErrorResponse errorResponse, HttpStatus status) {
        String responseJson = new Gson().toJson(errorResponse);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseJson, headers, status);
    }
}
