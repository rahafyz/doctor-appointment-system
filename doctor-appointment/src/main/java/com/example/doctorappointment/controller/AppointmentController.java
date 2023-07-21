package com.example.doctorappointment.controller;

import com.example.doctorappointment.dto.AppointmentDTO;
import com.example.doctorappointment.dto.ReserveAppointmentDTO;
import com.example.doctorappointment.service.AppointmentFacadeService;
import com.example.doctorappointment.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AppointmentFacadeService appointmentFacadeService;

    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(appointmentService.findAll(pageable));
    }

    @PostMapping
    public ResponseEntity<Void> reserve(@RequestBody @Valid ReserveAppointmentDTO reserveAppointmentDTO) {
        appointmentFacadeService.reserveAppointment(reserveAppointmentDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value="/test-gateway")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("hi");
    }
    
}