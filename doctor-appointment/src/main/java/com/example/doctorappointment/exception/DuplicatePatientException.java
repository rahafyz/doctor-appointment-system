package com.example.doctorappointment.exception;

import org.springframework.http.HttpStatus;

public class DuplicatePatientException extends CustomException{
    private static final String DESC = "blubank.patient.duplicate.exception";
    public DuplicatePatientException() {
        super(DESC,HttpStatus.NOT_ACCEPTABLE);
    }
}
