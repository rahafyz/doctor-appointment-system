package com.example.doctorappointment.mapper;

import com.example.doctorappointment.dto.AppointmentSlotDTO;
import com.example.doctorappointment.model.AppointmentSlot;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppointmentSlotMapper extends EntityMapper<AppointmentSlot,AppointmentSlotDTO>{

}
