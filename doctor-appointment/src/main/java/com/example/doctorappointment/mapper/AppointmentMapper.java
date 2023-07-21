package com.example.doctorappointment.mapper;

import com.example.doctorappointment.dto.AppointmentDTO;
import com.example.doctorappointment.dto.CreateAppointmentDTO;
import com.example.doctorappointment.model.Appointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentMapper extends EntityMapper<Appointment,AppointmentDTO>{
    Appointment toEntity(CreateAppointmentDTO createAppointmentDTO);
}
