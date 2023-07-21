package com.example.doctorappointment.service;

import com.example.doctorappointment.dto.AppointmentDTO;
import com.example.doctorappointment.dto.CreateAppointmentDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppointmentService {

    AppointmentDTO create(CreateAppointmentDTO createAppointmentDTO);
    List<AppointmentDTO> findAll(Pageable pageable);


}
