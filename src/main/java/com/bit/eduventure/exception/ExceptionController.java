package com.bit.eduventure.exception;

import com.bit.eduventure.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionController {
    ResponseDTO<?> responseDTO = new ResponseDTO<>();
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        responseDTO.setErrorMessage(e.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        System.out.println("RestControllerAdvice 실행");
        return ResponseEntity.badRequest().body(responseDTO);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noSuchElementExceptionHandler(NoSuchElementException e) {
//        responseDTO = new ResponseDTO<>();
        responseDTO.setErrorMessage(e.getMessage());
        responseDTO.setStatusCode(HttpStatus.BAD_REQUEST.value());
        System.out.println("RestControllerAdvice 실행");
        return ResponseEntity.badRequest().body(responseDTO);
    }
}