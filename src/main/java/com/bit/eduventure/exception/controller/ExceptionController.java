package com.bit.eduventure.exception.controller;


import com.bit.eduventure.exception.errorCode.ErrorCode;
import com.bit.eduventure.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionController {
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> exceptionHandler(Exception e) {
//        responseDTO.setErrorMessage(e.getMessage());
//        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        System.out.println("RestControllerAdvice 실행");
//        return ResponseEntity.badRequest().body(responseDTO);
//    }
//
//    @ExceptionHandler(NoSuchElementException.class)
//    public ResponseEntity<?> noSuchElementExceptionHandler(NoSuchElementException e) {
////        responseDTO = new ResponseDTO<>();
//        responseDTO.setErrorMessage(e.getMessage());
//        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
//        System.out.println("RestControllerAdvice 실행");
//        return ResponseEntity.badRequest().body(responseDTO);
//    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUserNameNotFoundException(){
        ErrorResponse response = new ErrorResponse(ErrorCode.BOARD_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}