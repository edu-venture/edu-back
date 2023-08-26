package com.bit.eduventure.exception.controller;


import com.bit.eduventure.exception.errorCode.ErrorCode;
import com.bit.eduventure.exception.response.ErrorResponse;
import org.json.simple.JSONObject;
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
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> exceptionHandler(Exception e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.EXCEPTION);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<String> noSuchElementExceptionHandler(NoSuchElementException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.NO_SUCH_ELEMENT);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<String> nullPointerExceptionHandler(NullPointerException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.NULL_POINT);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClassCastException.class)
    protected ResponseEntity<String> classCastExceptionHandler(ClassCastException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.CLASS_CAST);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StackOverflowError.class)
    protected ResponseEntity<String> stackOverFlowErrorHandler(StackOverflowError e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.STACK_OVER_FLOW);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataFormatException.class)
    protected ResponseEntity<String> dataFormatExceptionHandler(DataFormatException e) {
        ErrorResponse response = new ErrorResponse(ErrorCode.STACK_OVER_FLOW);
        return setResponse(response, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<String> setResponse(ErrorResponse errorResponse, HttpStatus status) {
        String responseJson = createResponseJson(errorResponse);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(responseJson, headers, status);
    }

    private String createResponseJson(ErrorResponse errorResponse) {
        JSONObject responseJson = new JSONObject();
        responseJson.put("errorMessage", errorResponse.getMessage());
        responseJson.put("statusCode", errorResponse.getStatus());
        return responseJson.toJSONString();
    }
}
