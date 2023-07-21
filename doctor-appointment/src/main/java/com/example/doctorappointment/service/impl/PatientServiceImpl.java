package com.example.doctorappointment.service.impl;

import com.example.doctorappointment.dto.CreatePatientDTO;
import com.example.doctorappointment.dto.PatientDTO;
import com.example.doctorappointment.exception.DuplicatePatientException;
import com.example.doctorappointment.exception.PatientNotFoundException;
import com.example.doctorappointment.mapper.PatientMapper;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.repository.PatientRepository;
import com.example.doctorappointment.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository repository;
    private final PatientMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Patient> get(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PatientDTO.AppointmentDTO> getAppointments(String phoneNumber) {
        Patient patient = this.get(phoneNumber).orElseThrow(
                PatientNotFoundException::new
        );
        return mapper.toDTO(patient).getAppointmentList();
    }

    @Override
    @Transactional
    public PatientDTO create(CreatePatientDTO patientDTO) {
        if (get(patientDTO.getPhoneNumber()).isPresent())
            throw new DuplicatePatientException();
        Patient patient = mapper.toEntity(patientDTO);
        return mapper.toDTO(repository.save(patient));
    }

    @Override
    @Transactional
    public PatientDTO update(Long id, PatientDTO patientDTO) {
        Patient patient = repository.findById(id).orElseThrow(
                PatientNotFoundException::new
        );
        mapper.partialUpdate(patient, patientDTO);
        return mapper.toDTO(repository.save(patient));
    }
}
