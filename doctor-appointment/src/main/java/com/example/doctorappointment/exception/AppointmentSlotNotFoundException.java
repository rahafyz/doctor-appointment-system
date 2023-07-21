package com.example.doctorappointment.exception;

import org.springframework.http.HttpStatus;

public class AppointmentSlotNotFoundException extends CustomException{
    private static final String DESC = "blubank.appointment.slot.not.found.exception";
    public AppointmentSlotNotFoundException() {
        super(DESC, HttpStatus.NOT_FOUND);
    }
}
