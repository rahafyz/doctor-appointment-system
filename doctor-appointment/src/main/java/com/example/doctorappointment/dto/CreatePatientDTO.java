package com.example.doctorappointment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class CreatePatientDTO {

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

}
