package com.example.doctorappointment.exception;

import org.springframework.http.HttpStatus;

public class ReservedAppointmentSlotException extends CustomException{
    private static final String DESC = "blubank.appointment.slot.exception";
    public ReservedAppointmentSlotException() {
        super(DESC, HttpStatus.NOT_ACCEPTABLE);
    }
}
