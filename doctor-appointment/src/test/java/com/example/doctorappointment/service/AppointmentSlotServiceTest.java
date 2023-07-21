package com.example.doctorappointment.service;

import com.example.doctorappointment.dto.AppointmentSlotDTO;
import com.example.doctorappointment.exception.AppointmentSlotNotFoundException;
import com.example.doctorappointment.exception.ConcurrentRequestException;
import com.example.doctorappointment.exception.InvalidTimeException;
import com.example.doctorappointment.exception.ReservedAppointmentSlotException;
import com.example.doctorappointment.mapper.AppointmentSlotMapperImpl;
import com.example.doctorappointment.model.AppointmentSlot;
import com.example.doctorappointment.repository.AppointmentSlotRepository;
import com.example.doctorappointment.service.impl.AppointmentSlotServiceImpl;
import com.example.doctorappointment.util.LockUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.doctorappointment.util.AppointmentSlotData.*;
import static com.example.doctorappointment.util.DoctorData.doctor;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentSlotServiceTest {

    @Mock
    private AppointmentSlotRepository repository;
    @Spy
    private AppointmentSlotMapperImpl mapper;
    @Mock
    private LockUtil lockUtil;

    @Mock
    DoctorService doctorService;

    private AppointmentSlotService service;
    private static final Long ID = 1L;

    private static final Pageable pageable = PageRequest.of(0, 1);

    @BeforeEach
    void init() {
        service = new AppointmentSlotServiceImpl(repository, lockUtil, mapper, doctorService);
        ReflectionTestUtils.setField(service, "timeInterval", 30);
    }

    @Test
    void getById_shouldReturnAppointmentSlot() {

        when(repository.findById(ID)).thenReturn(Optional.of(appointmentSlot()));

        AppointmentSlot appointmentSlot = service.getById(ID);

        Assertions.assertEquals(appointmentSlot(), appointmentSlot);
        Assertions.assertNotNull(appointmentSlot().getId());
        Assertions.assertEquals(appointmentSlot().getStartTime(), appointmentSlot.getStartTime());
        Assertions.assertEquals(appointmentSlot().getEndTime(), appointmentSlot.getEndTime());
        Assertions.assertEquals(appointmentSlot().getIsAvailable(), appointmentSlot.getIsAvailable());
    }

    @Test
    void getById_whenNotFound_shouldThrowException() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(AppointmentSlotNotFoundException.class, () -> service.getById(ID));
    }

    @Test
    void save_shouldReturnAppointmentSlotDTOList() {
        List<AppointmentSlot> appointmentSlotList = List.of(appointmentSlotWithoutId());

        when(doctorService.getById(ID)).thenReturn(doctor());

        when(repository.saveAll(appointmentSlotList)).thenReturn(appointmentSlotList());

        Assertions.assertEquals(appointmentSlotDTOList(), service.save(createAppointmentSlotDTO()));

        verify(repository, times(1)).saveAll(appointmentSlotList);
    }

    @Test
    void save_whenInvalidTime_shouldThrowException() {
        Assertions.assertThrows(InvalidTimeException.class, () -> service.save(createInvalidAppointmentSlotDTO()));
    }

    @Test
    void save_whenShortTime_shouldAddNothing() {

        Assertions.assertEquals(Collections.emptyList(), service.save(createShortAppointmentSlotDTO()));

        verify(repository, times(0)).saveAll(List.of(appointmentSlot()));
    }

    @Test
    void getOpenAppointments_shouldReturnAppointmentSlotDTOList() {
        repository.saveAll(List.of(appointmentSlot(), takenAppointmentSlot()));
        when(repository.findByDoctor_IdAndIsAvailableTrue(ID, pageable)).thenReturn(appointmentSlotList());

        List<AppointmentSlotDTO> openAppointments = service.getOpenAppointments(ID, pageable);

        Assertions.assertEquals(appointmentSlotDTOList(), openAppointments);

        Assertions.assertArrayEquals(appointmentSlotDTOList().toArray(), openAppointments.toArray());

        Assertions.assertEquals(appointmentSlotDTOList().size(), openAppointments.size());

    }

    @Test
    void getOpenAppointments_whenNoOpenAppointment_shouldReturnEmptyList() {
        when(repository.findByDoctor_IdAndIsAvailableTrue(ID, pageable)).thenReturn(Collections.EMPTY_LIST);

        Assertions.assertEquals(Collections.EMPTY_LIST, service.getOpenAppointments(ID, pageable));

    }

    @Test
    void delete_shouldDeleteAppointment() {

        // Mock lock and lockUtil
        RLock mockLock = mock(RLock.class);
        when(lockUtil.getLockForAppointmentSlot(1L)).thenReturn(mockLock);

        when(repository.findById(ID)).thenReturn(Optional.ofNullable(appointmentSlot()));

        service.delete(ID);

        verify(repository).deleteById(ID);

    }

    @Test
    void delete_whenAppointmentNotFound_shouldThrowException() {
        RLock mockLock = mock(RLock.class);
        when(lockUtil.getLockForAppointmentSlot(ID)).thenReturn(mockLock);
        when(repository.findById(ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(AppointmentSlotNotFoundException.class, () -> service.delete(ID));
    }

    @Test
    void delete_whenAppointmentIsTaken_shouldThrowException() {
        RLock mockLock = mock(RLock.class);
        when(lockUtil.getLockForAppointmentSlot(ID)).thenReturn(mockLock);
        when(repository.findById(ID)).thenReturn(Optional.ofNullable(takenAppointmentSlot()));

        Assertions.assertThrows(ReservedAppointmentSlotException.class, () -> service.delete(ID));
    }


    @Test
    void delete_whenLockIsTaken_shouldThrowException() {
        when(lockUtil.getLockForAppointmentSlot(ID)).thenReturn(null);

        Assertions.assertThrows(ConcurrentRequestException.class, () -> service.delete(ID));
    }

    @Test
    void getByDate_shouldReturnAppointmentSlotDTOList() {
        when(repository.findByIsAvailableAndStartTimeBetween(true, DATE.atStartOfDay(), DATE.atTime(23, 59), pageable))
                .thenReturn(appointmentSlotList());

        List<AppointmentSlotDTO> appointmentSlotDTOS = service.getByDate(LocalDate.now(), pageable);

        Assertions.assertEquals(appointmentSlotDTOList(), appointmentSlotDTOS);

        Assertions.assertArrayEquals(appointmentSlotDTOList().toArray(), appointmentSlotDTOS.toArray());

        Assertions.assertEquals(appointmentSlotDTOList().size(), appointmentSlotDTOS.size());
    }

    @Test
    void getByDate_whenNoOpenAppointment_shouldReturnEmptyList() {
        when(repository.findByIsAvailableAndStartTimeBetween(true, DATE.atStartOfDay(), DATE.atTime(23, 59), pageable))
                .thenReturn(Collections.EMPTY_LIST);

        Assertions.assertEquals(Collections.EMPTY_LIST, service.getByDate(LocalDate.now(), pageable));
    }

}
