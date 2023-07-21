package com.example.doctorappointment.util;

import com.example.doctorappointment.dto.AppointmentDTO;
import com.example.doctorappointment.dto.CreateAppointmentDTO;
import com.example.doctorappointment.dto.PatientDTO;
import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.model.Patient;

import java.util.List;

public class AppointmentData {


    public static Appointment appointment() {
        return Appointment.builder()
                .id(1L)
                .appointmentSlot(AppointmentSlotData.appointmentSlotData())
                .patient(Patient.builder().id(1L).name(PatientData.NAME).phoneNumber(PatientData.PHONE_NUMBER).build())
                .build();
    }

    public static Appointment appointmentWithoutId() {
        return Appointment.builder()
                .appointmentSlot(AppointmentSlotData.takenAppointmentSlot())
                .patient(Patient.builder().id(1L).name(PatientData.NAME).phoneNumber(PatientData.PHONE_NUMBER)
                        .appointmentList(
                                List.of(Appointment.builder().appointmentSlot(AppointmentSlotData.appointmentSlotData()).build())
                        ).build())
                .build();
    }

    public static AppointmentDTO appointmentDTO() {
        return AppointmentDTO.builder()
                .id(1L)
                .appointmentSlot(AppointmentSlotData.takenAppointmentSlotDTO())
                .patient(AppointmentDTO.PatientDTO.builder().name(PatientData.NAME).phoneNumber(PatientData.PHONE_NUMBER).build())
                .build();
    }

    public static List<Appointment> appointmentList() {
        return List.of(appointment());
    }

    public static CreateAppointmentDTO createAppointmentDTO() {
        return CreateAppointmentDTO.builder()
                .appointmentSlot(AppointmentSlotData.takenAppointmentSlot())
                .patient(PatientDTO.builder().id(1L).name(PatientData.NAME).phoneNumber(PatientData.PHONE_NUMBER)
                        .appointmentList(
                                List.of(PatientDTO.AppointmentDTO.builder().appointmentSlot(AppointmentSlotData.takenAppointmentSlotDTO()).build())
                        ).build())
                .build();
    }
}
