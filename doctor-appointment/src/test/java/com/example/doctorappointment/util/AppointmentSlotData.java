package com.example.doctorappointment.util;

import com.example.doctorappointment.dto.AppointmentSlotDTO;
import com.example.doctorappointment.dto.CreateAppointmentSlotDTO;
import com.example.doctorappointment.model.AppointmentSlot;
import com.example.doctorappointment.model.Doctor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.doctorappointment.util.DoctorData.doctor;

public class AppointmentSlotData {

    public static final LocalDate DATE = LocalDate.now();
    private static final LocalDateTime START_TIME = DATE.atStartOfDay();
    private static final LocalDateTime END_TIME = START_TIME.plusMinutes(30);
    private static final LocalDateTime CREATE_START_TIME = DATE.atStartOfDay();
    private static final LocalDateTime CREATE_END_TIME = CREATE_START_TIME.plusMinutes(33);
    private static final LocalDateTime INVALID_CREATE_END_TIME = DATE.atStartOfDay().minusHours(1);

    public static AppointmentSlot appointmentSlot() {
        return AppointmentSlot.builder()
                .id(1L)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .isAvailable(true)
                .doctor(doctor())
                .build();
    }

    public static AppointmentSlot appointmentSlot(Doctor doctor) {
        return AppointmentSlot.builder()
                .id(1L)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .isAvailable(true)
                .doctor(doctor)
                .build();
    }

    public static AppointmentSlot appointmentSlotData() {
        return AppointmentSlot.builder()
                .id(1L)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .isAvailable(false)
                .build();
    }

    public static AppointmentSlot appointmentSlotWithoutId() {
        return AppointmentSlot.builder()
                .startTime(START_TIME)
                .endTime(END_TIME)
                .isAvailable(true)
                .doctor(doctor())
                .build();
    }

    public static AppointmentSlot takenAppointmentSlot() {
        return AppointmentSlot.builder()
                .id(1L)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .isAvailable(false)
                .doctor(doctor())
                .build();
    }

    public static AppointmentSlotDTO appointmentSlotDTO() {
        return AppointmentSlotDTO.builder()
                .id(1L)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .isAvailable(true)
                .build();
    }
    public static AppointmentSlotDTO takenAppointmentSlotDTO() {
        return AppointmentSlotDTO.builder()
                .id(1L)
                .startTime(START_TIME)
                .endTime(END_TIME)
                .isAvailable(false)
                .build();
    }

    public static List<AppointmentSlotDTO> appointmentSlotDTOList() {
        return List.of(appointmentSlotDTO());
    }

    public static List<AppointmentSlot> appointmentSlotList() {
        return List.of(appointmentSlot());
    }

    public static CreateAppointmentSlotDTO createAppointmentSlotDTO() {
        return CreateAppointmentSlotDTO.builder()
                .startTime(CREATE_START_TIME)
                .endTime(CREATE_END_TIME)
                .doctorId(1L).build();
    }

    public static CreateAppointmentSlotDTO createInvalidAppointmentSlotDTO() {
        return CreateAppointmentSlotDTO.builder()
                .startTime(CREATE_START_TIME)
                .endTime(INVALID_CREATE_END_TIME).build();
    }

    public static CreateAppointmentSlotDTO createShortAppointmentSlotDTO() {
        return CreateAppointmentSlotDTO.builder()
                .startTime(CREATE_START_TIME)
                .endTime(CREATE_START_TIME.plusMinutes(10)).build();
    }
}
