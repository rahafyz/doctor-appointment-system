package com.example.doctorappointment.service;

import com.example.doctorappointment.dto.ReserveAppointmentDTO;

public interface AppointmentFacadeService {
    void reserveAppointment(ReserveAppointmentDTO reserveDTO);
}
