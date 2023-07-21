package com.example.doctorappointment.exception;

import org.springframework.http.HttpStatus;

public class PatientNotFoundException extends CustomException{
    private static final String DESC = "blubank.patient.not.found.exception";
    public PatientNotFoundException() {
        super(DESC, HttpStatus.NOT_FOUND);
    }
}
