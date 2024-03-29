package com.example.doctorappointment.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class CustomException extends RuntimeException{

    private final String message;
    private final HttpStatus httpStatus;


    @Override
    public String getMessage() {
        return message;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
