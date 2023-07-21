package com.example.doctorappointment.service;

import com.example.doctorappointment.dto.AppointmentDTO;
import com.example.doctorappointment.dto.CreateAppointmentDTO;
import com.example.doctorappointment.exception.ConcurrentRequestException;
import com.example.doctorappointment.exception.ReservedAppointmentSlotException;
import com.example.doctorappointment.mapper.AppointmentSlotMapperImpl;
import com.example.doctorappointment.mapper.PatientMapperImpl;
import com.example.doctorappointment.model.AppointmentSlot;
import com.example.doctorappointment.service.impl.AppointmentFacadeServiceImpl;
import com.example.doctorappointment.util.LockUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;

import java.util.Optional;

import static com.example.doctorappointment.util.AppointmentData.appointmentDTO;
import static com.example.doctorappointment.util.AppointmentData.createAppointmentDTO;
import static com.example.doctorappointment.util.AppointmentFacadeData.reserveAppointmentDTO;
import static com.example.doctorappointment.util.AppointmentSlotData.*;
import static com.example.doctorappointment.util.PatientData.patient;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentFacadeServiceTest {

    @Mock
    private PatientService patientService;

    @Spy
    private PatientMapperImpl patientMapper;

    @Mock
    private AppointmentSlotService appointmentSlotService;

    @Spy
    private AppointmentSlotMapperImpl appointmentSlotMapper;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private LockUtil lockUtil;

    private AppointmentFacadeService facadeService;

    @BeforeEach
    void init() {
        facadeService = new AppointmentFacadeServiceImpl(patientService, patientMapper,
                appointmentSlotService, appointmentSlotMapper, appointmentService, lockUtil);
    }

    @Test
    void reserve() {
        AppointmentSlot appointmentSlot = appointmentSlot();
        CreateAppointmentDTO createAppointmentDTO = createAppointmentDTO();
        AppointmentDTO appointmentDTO = appointmentDTO();


        RLock mockLock = mock(RLock.class);
        when(lockUtil.getLockForAppointmentSlot(1L)).thenReturn(mockLock);


        when(patientService.get(reserveAppointmentDTO().getPatientDTO().getPhoneNumber())).thenReturn(Optional.of(patient()));

        when(appointmentSlotService.getById(reserveAppointmentDTO().getAppointmentSlotId())).thenReturn(appointmentSlot());

        appointmentSlot.setIsAvailable(false);
        createAppointmentDTO.setAppointmentSlot(appointmentSlot);


        when(appointmentService.create(createAppointmentDTO)).thenReturn(appointmentDTO);

        facadeService.reserveAppointment(reserveAppointmentDTO());

        verify(patientService).get(reserveAppointmentDTO().getPatientDTO().getPhoneNumber());
        verify(appointmentSlotService).getById(reserveAppointmentDTO().getAppointmentSlotId());

    }

    @Test
    void reserve_whenLockIsTaken_shouldThrowException() {
        when(lockUtil.getLockForAppointmentSlot(1L)).thenReturn(null);

        Assertions.assertThrows(ConcurrentRequestException.class, () -> facadeService.reserveAppointment(reserveAppointmentDTO()));
    }

    @Test
    void reserve_whenAppointmentIsTaken_shouldThrowException() {
        RLock mockLock = mock(RLock.class);
        when(lockUtil.getLockForAppointmentSlot(1L)).thenReturn(mockLock);


        when(patientService.get(reserveAppointmentDTO().getPatientDTO().getPhoneNumber())).thenReturn(Optional.of(patient()));

        when(appointmentSlotService.getById(reserveAppointmentDTO().getAppointmentSlotId())).thenReturn(takenAppointmentSlot());

        Assertions.assertThrows(ReservedAppointmentSlotException.class, () -> facadeService.reserveAppointment(reserveAppointmentDTO()));

    }
}
