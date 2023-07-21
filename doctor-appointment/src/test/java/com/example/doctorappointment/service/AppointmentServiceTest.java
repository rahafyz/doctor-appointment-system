package com.example.doctorappointment.service;

import com.example.doctorappointment.dto.AppointmentDTO;
import com.example.doctorappointment.mapper.AppointmentMapperImpl;
import com.example.doctorappointment.repository.AppointmentRepository;
import com.example.doctorappointment.service.impl.AppointmentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static com.example.doctorappointment.util.AppointmentData.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @Spy
    private AppointmentMapperImpl mapper;

    private AppointmentService service;

    private static final Pageable pageable = PageRequest.of(0, 1);

    @BeforeEach
    void init() {
        service = new AppointmentServiceImpl(repository, mapper);
    }

    @Test
    void findAll_shouldReturnAppointmentDTOList() {
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(appointmentList()));

        List<AppointmentDTO> appointmentDTOS = service.findAll(pageable);

        Assertions.assertEquals(List.of(appointmentDTO()), appointmentDTOS);

        Assertions.assertArrayEquals(List.of(appointmentDTO()).toArray(), appointmentDTOS.toArray());

        Assertions.assertEquals(List.of(appointmentDTO()).size(), appointmentDTOS.size());
    }

    @Test
    void findAll_whenNoAppointment_shouldReturnEmptyList() {
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));

        Assertions.assertEquals(Collections.EMPTY_LIST, service.findAll(pageable));
    }

    @Test
    void save_shouldReturnAppointmentDTO() {

        when(repository.save(appointmentWithoutId())).thenReturn(appointment());

        Assertions.assertEquals(appointmentDTO(), service.create(createAppointmentDTO()));

    }
}
