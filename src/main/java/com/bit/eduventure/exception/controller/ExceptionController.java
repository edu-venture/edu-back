package com.bit.eduventure.exception.controller;


import com.bit.eduventure.exception.errorCode.ErrorCode;
import com.bit.eduventure.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.EXCEPTION);
        System.out.println("RestControllerAdvice 실행");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<ErrorResponse> noSuchElementExceptionHandler(NoSuchElementException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.EXCEPTION);
        System.out.println("RestControllerAdvice 실행");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(value = UsernameNotFoundException.class)
//    protected ResponseEntity<ErrorResponse> handleUserNameNotFoundException(){
//        ErrorResponse response = new ErrorResponse(ErrorCode.BOARD_NOT_FOUND);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }
}