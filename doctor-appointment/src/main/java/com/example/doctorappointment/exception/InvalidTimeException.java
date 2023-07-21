package com.example.doctorappointment.exception;

import org.springframework.http.HttpStatus;

public class InvalidTimeException extends CustomException{
    private static final String DESC = "blubank.appointment.slot.invalid.time.exception";
    public InvalidTimeException() {
        super(DESC, HttpStatus.BAD_REQUEST);
    }
}
