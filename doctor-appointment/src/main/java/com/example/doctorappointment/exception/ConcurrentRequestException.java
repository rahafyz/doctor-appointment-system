package com.example.doctorappointment.exception;

import org.springframework.http.HttpStatus;

public class ConcurrentRequestException extends CustomException {

    private static final String DESC = "blubank.concurrent.exception";

    public ConcurrentRequestException() {
        super(DESC,HttpStatus.TOO_MANY_REQUESTS);
    }

}