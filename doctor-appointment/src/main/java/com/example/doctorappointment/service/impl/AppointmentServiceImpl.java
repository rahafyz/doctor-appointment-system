package com.example.doctorappointment.service.impl;

import com.example.doctorappointment.dto.AppointmentDTO;
import com.example.doctorappointment.dto.CreateAppointmentDTO;
import com.example.doctorappointment.mapper.AppointmentMapper;
import com.example.doctorappointment.model.Appointment;
import com.example.doctorappointment.repository.AppointmentRepository;
import com.example.doctorappointment.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repository;
    private final AppointmentMapper mapper;

    @Override
    @Transactional
    public AppointmentDTO create(CreateAppointmentDTO createAppointmentDTO) {
        Appointment appointment = mapper.toEntity(createAppointmentDTO);
        return mapper.toDTO(repository.save(appointment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentDTO> findAll(Pageable pageable) {
        return mapper.toDTOList(repository.findAll(pageable).getContent());
    }
}
