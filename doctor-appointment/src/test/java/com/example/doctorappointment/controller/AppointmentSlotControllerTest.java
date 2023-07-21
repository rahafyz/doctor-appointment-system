package com.example.doctorappointment.controller;


import com.example.doctorappointment.dto.CreateAppointmentSlotDTO;
import com.example.doctorappointment.exception.GlobalExceptionHandling;
import com.example.doctorappointment.model.AppointmentSlot;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.repository.AppointmentSlotRepository;
import com.example.doctorappointment.repository.DoctorRepository;
import com.example.doctorappointment.service.AppointmentSlotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.example.doctorappointment.util.AppointmentSlotData.appointmentSlot;
import static com.example.doctorappointment.util.AppointmentSlotData.createAppointmentSlotDTO;
import static com.example.doctorappointment.util.DoctorData.doctor;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentSlotControllerTest {

    @Autowired
    private AppointmentSlotRepository repository;

    @Autowired
    private AppointmentSlotService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    GlobalExceptionHandling exceptionHandler;

    @Autowired
    private DoctorRepository doctorRepository;

    private MockMvc mockMvc;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int pageNumber = 0;
    private static final int pageSize = 10;

    @BeforeEach
    void setup() {
        AppointmentSlotController controller = new AppointmentSlotController(service);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(jacksonMessageConverter)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(exceptionHandler)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        repository.deleteAll();
        doctorRepository.deleteAll();
    }

    @Test
    void save() throws Exception {

        Doctor doctor = doctorRepository.save(doctor());

        CreateAppointmentSlotDTO createAppointmentSlotDTO = createAppointmentSlotDTO();
        createAppointmentSlotDTO.setDoctorId(doctor.getId());

        mockMvc.perform(post("/api/v1/appointment-slot")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAppointmentSlotDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].startTime").value(createAppointmentSlotDTO.getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(createAppointmentSlotDTO.getStartTime().plusMinutes(30).format(formatter)))
                .andExpect(jsonPath("$[0].isAvailable").value(true));
    }

    @Test
    void getOpenAppointments() throws Exception {
        Doctor doctor = doctorRepository.save(doctor());
        repository.save(appointmentSlot(doctor));

        mockMvc.perform(get("/api/v1/appointment-slot/appointments/{doctorId}", 1L)
                        .param("page", String.valueOf(pageNumber))
                        .param("size", String.valueOf(pageSize)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].startTime").value(appointmentSlot().getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(appointmentSlot().getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].isAvailable").value(appointmentSlot().getIsAvailable()));

    }

    @Test
    void getByDate() throws Exception {
        Doctor doctor = doctorRepository.save(doctor());
        repository.save(appointmentSlot(doctor));

        mockMvc.perform(
                        get("/api/v1/appointment-slot/appointments")
                                .param("date", LocalDate.now().toString())
                                .param("page", String.valueOf(pageNumber))
                                .param("size", String.valueOf(pageSize)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startTime").value(appointmentSlot().getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].endTime").value(appointmentSlot().getEndTime().format(formatter)))
                .andExpect(jsonPath("$[0].isAvailable").value(appointmentSlot().getIsAvailable()))
                .andReturn();

    }

    @Test
    void testDelete() throws Exception {

        Doctor doctor = doctorRepository.save(doctor());
        AppointmentSlot appointmentSlot = repository.save(appointmentSlot(doctor));

        long countBeforeDelete = repository.count();

        mockMvc.perform(delete("/api/v1/appointment-slot/{id}", appointmentSlot.getId()))
                .andExpect(status().isOk())
                .andReturn();

        long countAfterDelete = repository.count();

        assertEquals(countAfterDelete, countBeforeDelete - 1);

    }
}
