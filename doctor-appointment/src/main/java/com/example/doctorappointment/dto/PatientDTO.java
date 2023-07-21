package com.example.doctorappointment.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class PatientDTO {

    private Long id;

    private String name;

    private String phoneNumber;

    private List<AppointmentDTO> appointmentList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @EqualsAndHashCode
    public static class AppointmentDTO{
        private AppointmentSlotDTO appointmentSlot;
    }

}
