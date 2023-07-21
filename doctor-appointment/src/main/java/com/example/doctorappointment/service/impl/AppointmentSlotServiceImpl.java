package com.example.doctorappointment.service.impl;

import com.example.doctorappointment.dto.AppointmentSlotDTO;
import com.example.doctorappointment.dto.CreateAppointmentSlotDTO;
import com.example.doctorappointment.exception.AppointmentSlotNotFoundException;
import com.example.doctorappointment.exception.ConcurrentRequestException;
import com.example.doctorappointment.exception.InvalidTimeException;
import com.example.doctorappointment.exception.ReservedAppointmentSlotException;
import com.example.doctorappointment.mapper.AppointmentSlotMapper;
import com.example.doctorappointment.model.AppointmentSlot;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.repository.AppointmentSlotRepository;
import com.example.doctorappointment.service.AppointmentSlotService;
import com.example.doctorappointment.service.DoctorService;
import com.example.doctorappointment.util.LockUtil;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private final AppointmentSlotRepository repository;
    private final LockUtil lockUtil;
    private final AppointmentSlotMapper mapper;
    private final DoctorService doctorService;

    private final Predicate<CreateAppointmentSlotDTO> isTimeValid = createAppointmentSlotDTO ->
            createAppointmentSlotDTO.getStartTime().isBefore(createAppointmentSlotDTO.getEndTime());

    private final Predicate<AppointmentSlot> isAvailable = appointmentSlot -> Boolean.TRUE.equals(appointmentSlot.getIsAvailable());

    @Value("${doctor.appointment.time.interval}")
    private Integer timeInterval = 30;

    @Override
    @Transactional(readOnly = true)
    public AppointmentSlot getById(Long id) {
        return repository.findById(id).orElseThrow(
                AppointmentSlotNotFoundException::new
        );
    }

    @Override
    @Transactional
    public List<AppointmentSlotDTO> save(CreateAppointmentSlotDTO dto) {
        if (!isTimeValid.test(dto))
            throw new InvalidTimeException();
        if (setTimeInterval(dto).isEmpty())
            return Collections.emptyList();
        return mapper.toDTOList(repository.saveAll(setTimeInterval(dto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSlotDTO> getOpenAppointments(Long doctorId, Pageable pageable) {
        return mapper.toDTOList(repository.findByDoctor_IdAndIsAvailableTrue(doctorId, pageable));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        RLock lock = lockUtil.getLockForAppointmentSlot(id);
        try {
            if (Objects.nonNull(lock)) {
                AppointmentSlot appointmentSlot = getById(id);

                if (!isAvailable.test(appointmentSlot))
                    throw new ReservedAppointmentSlotException();
                repository.deleteById(id)
                ;
            } else {
                throw new ConcurrentRequestException();
            }
        } finally {
            lockUtil.releaseLockForAppointmentSlotTimeSlot(id);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentSlotDTO> getByDate(LocalDate date, Pageable pageable) {
        LocalDateTime startTime = date.atStartOfDay();
        LocalDateTime endTime = date.atTime(23, 59);
        List<AppointmentSlot> appointmentSlots = repository.findByIsAvailableAndStartTimeBetween(true, startTime, endTime, pageable);
        return mapper.toDTOList(appointmentSlots);
    }


    private List<AppointmentSlot> setTimeInterval(CreateAppointmentSlotDTO dto) {
        Doctor doctor = getDoctor(dto.getDoctorId());

        List<AppointmentSlot> timeSlots = new ArrayList<>();

        long numSlots = Duration.between(dto.getStartTime(), dto.getEndTime()).toMinutes() / timeInterval;

        if (numSlots == 0)
            return timeSlots;

        LocalDateTime slotTime = dto.getStartTime();
        for (int i = 0; i < numSlots; i++) {
            AppointmentSlot slot = AppointmentSlot.builder()
                    .startTime(slotTime)
                    .endTime(slotTime.plusMinutes(30))
                    .doctor(doctor)
                    .isAvailable(true).build();
            timeSlots.add(slot);
            slotTime = slotTime.plusMinutes(30);
        }
        return timeSlots;
    }

    private Doctor getDoctor(Long doctorId) {
        return doctorService.getById(doctorId);
    }
}