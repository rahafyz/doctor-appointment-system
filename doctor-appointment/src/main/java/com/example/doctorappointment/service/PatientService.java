package com.example.doctorappointment.service;

import com.example.doctorappointment.dto.CreatePatientDTO;
import com.example.doctorappointment.dto.PatientDTO;
import com.example.doctorappointment.model.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {

    Optional<Patient> get(String phoneNumber);
    List<PatientDTO.AppointmentDTO> getAppointments(String phoneNumber);

    PatientDTO create(CreatePatientDTO patientDTO);

    PatientDTO update(Long id, PatientDTO patientDTO);
}
