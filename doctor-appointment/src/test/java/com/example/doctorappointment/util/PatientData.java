package com.example.doctorappointment.util;

import com.example.doctorappointment.dto.CreatePatientDTO;
import com.example.doctorappointment.dto.PatientDTO;
import com.example.doctorappointment.model.Patient;

import java.util.List;

import static com.example.doctorappointment.util.AppointmentData.appointment;

public class PatientData {
    public static final String NAME = "name";
    public static final String PHONE_NUMBER = "09121234567";

    public static Patient patient() {
        return Patient.builder()
                .id(1L)
                .name(NAME)
                .phoneNumber(PHONE_NUMBER)
                .appointmentList(List.of(appointment())).build();
    }
    public static Patient patientData() {
        return Patient.builder()
                .id(1L)
                .name(NAME)
                .phoneNumber(PHONE_NUMBER).build();
    }

    public static PatientDTO patientDTO() {
        return PatientDTO.builder()
                .id(1L)
                .name(NAME)
                .phoneNumber(PHONE_NUMBER)
                .appointmentList(List.of(PatientDTO.AppointmentDTO.builder().appointmentSlot(AppointmentSlotData.takenAppointmentSlotDTO()).build())).build();
    }

    public static PatientDTO patientDTOCreated() {
        return PatientDTO.builder()
                .id(1L)
                .name(NAME)
                .phoneNumber(PHONE_NUMBER)
                .build();
    }

    public static Patient patientCreated() {
        return Patient.builder()
                .id(1L)
                .name(NAME)
                .phoneNumber(PHONE_NUMBER)
                .build();
    }

    public static Patient patientCreatedWithoutId() {
        return Patient.builder()
                .name(NAME)
                .phoneNumber(PHONE_NUMBER)
                .build();
    }

    public static CreatePatientDTO createPatientDTO() {
        return CreatePatientDTO.builder()
                .name(NAME)
                .phoneNumber(PHONE_NUMBER)
                .build();
    }


}
