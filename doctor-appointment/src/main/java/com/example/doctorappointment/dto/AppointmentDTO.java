package com.example.doctorappointment.dto;

import lombok.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class AppointmentDTO {
    private Long id;
    private AppointmentSlotDTO appointmentSlot;
    private PatientDTO patient;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    @Builder
    public static class PatientDTO{
        private String name;
        private String phoneNumber;
    }
}
