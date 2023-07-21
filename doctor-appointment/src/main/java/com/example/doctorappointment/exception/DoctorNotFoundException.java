package com.example.doctorappointment.exception;

import org.springframework.http.HttpStatus;

public class DoctorNotFoundException extends CustomException{

    private static final String DESC = "blubank.doctor.not.found.exception";
    public DoctorNotFoundException() {
        super(DESC, HttpStatus.NOT_FOUND);
    }
}
