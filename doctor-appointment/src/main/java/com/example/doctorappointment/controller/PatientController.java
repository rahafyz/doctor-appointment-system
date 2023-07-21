package com.example.doctorappointment.controller;

import com.example.doctorappointment.dto.PatientDTO;
import com.example.doctorappointment.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/patient")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/appointments")
    public ResponseEntity<List<PatientDTO.AppointmentDTO>> getAppointments(@RequestParam String phoneNumber){
        return ResponseEntity.ok(patientService.getAppointments(phoneNumber));
    }

}
