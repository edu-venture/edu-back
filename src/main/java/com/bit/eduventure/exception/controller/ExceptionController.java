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

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementExceptionHandler(NoSuchElementException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.NO_SUCH_ELEMENT);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> nullPointerExceptionHandler(NullPointerException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.NULL_POINT);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<String> classCastExceptionHandler(ClassCastException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.CLASS_CAST);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StackOverflowError.class)
    public ResponseEntity<String> stackOverFlowErrorHandler(StackOverflowError e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.STACK_OVER_FLOW);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity<String> dataFormatExceptionHandler(DataFormatException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.DATA_FORMAT);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeExceptionHandler(RuntimeException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> illegalStateExceptionHandler(IllegalStateException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage());
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.EXCEPTION);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }



    private ResponseEntity<String> setResponse(ErrorResponse errorResponse, HttpStatus status) {
        String responseJson = createResponseJson(errorResponse);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseJson, headers, status);
    }

    private String createResponseJson(ErrorResponse errorResponse) {
        return new Gson().toJson(errorResponse);
    }
}

