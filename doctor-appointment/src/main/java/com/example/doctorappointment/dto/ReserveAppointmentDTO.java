package com.example.doctorappointment.dto;

import lombok.*;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveAppointmentDTO {

    @NotNull
    private CreatePatientDTO patientDTO;

    @NotNull
    private Long appointmentSlotId;

}
