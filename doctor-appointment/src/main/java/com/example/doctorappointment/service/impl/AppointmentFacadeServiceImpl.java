package com.example.doctorappointment.service.impl;

import com.example.doctorappointment.dto.CreateAppointmentDTO;
import com.example.doctorappointment.dto.CreatePatientDTO;
import com.example.doctorappointment.dto.PatientDTO;
import com.example.doctorappointment.dto.ReserveAppointmentDTO;
import com.example.doctorappointment.exception.ConcurrentRequestException;
import com.example.doctorappointment.exception.ReservedAppointmentSlotException;
import com.example.doctorappointment.mapper.AppointmentSlotMapper;
import com.example.doctorappointment.mapper.PatientMapper;
import com.example.doctorappointment.model.AppointmentSlot;
import com.example.doctorappointment.model.Patient;
import com.example.doctorappointment.service.AppointmentFacadeService;
import com.example.doctorappointment.service.AppointmentService;
import com.example.doctorappointment.service.AppointmentSlotService;
import com.example.doctorappointment.service.PatientService;
import com.example.doctorappointment.util.LockUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentFacadeServiceImpl implements AppointmentFacadeService {

    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final AppointmentSlotService appointmentSlotService;
    private final AppointmentSlotMapper appointmentSlotMapper;
    private final AppointmentService appointmentService;
    private final LockUtil lockUtil;

    @Override
    @Transactional
    public void reserveAppointment(ReserveAppointmentDTO reserveDTO) {
        RLock lock = lockUtil.getLockForAppointmentSlot(reserveDTO.getAppointmentSlotId());
        if (lock != null) {
            try {
                PatientDTO patientDTO = createPatient(reserveDTO.getPatientDTO());

                AppointmentSlot appointmentSlot = getAppointmentSlot(reserveDTO.getAppointmentSlotId());

                appointmentSlot.setIsAvailable(false);

                appointmentService.create(CreateAppointmentDTO.builder()
                        .appointmentSlot(appointmentSlot)
                        .patient(patientDTO).build());

            } finally {
                lockUtil.releaseLockForAppointmentSlotTimeSlot(reserveDTO.getAppointmentSlotId());
            }
        } else {
            throw new ConcurrentRequestException();
        }
    }

    private PatientDTO createPatient(CreatePatientDTO patientDTO) {
        Optional<Patient> patient = patientService.get(patientDTO.getPhoneNumber());
        if (patient.isPresent())
            return patientMapper.toDTO(patient.get());
        return patientService.create(patientDTO);
    }

    private AppointmentSlot getAppointmentSlot(Long appointmentSlotId) {
        AppointmentSlot appointmentSlot = appointmentSlotService.getById(appointmentSlotId);
        if (Boolean.TRUE.equals(appointmentSlot.getIsAvailable()))
            return appointmentSlot;
        throw new ReservedAppointmentSlotException();
    }
}