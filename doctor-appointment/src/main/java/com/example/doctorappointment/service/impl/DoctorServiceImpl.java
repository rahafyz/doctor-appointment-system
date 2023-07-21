package com.example.doctorappointment.service.impl;

import com.example.doctorappointment.exception.DoctorNotFoundException;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.repository.DoctorRepository;
import com.example.doctorappointment.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository repository;
    @Override
    public Doctor getById(Long id) {
        return repository.findById(id).orElseThrow(DoctorNotFoundException::new);
    }
}
