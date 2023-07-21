package com.example.doctorappointment.mapper;

import com.example.doctorappointment.dto.CreatePatientDTO;
import com.example.doctorappointment.dto.PatientDTO;
import com.example.doctorappointment.model.Patient;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientMapper extends EntityMapper<Patient, PatientDTO> {
    Patient toEntity(CreatePatientDTO dto);
}
