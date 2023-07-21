package com.example.doctorappointment.controller;

import com.example.doctorappointment.exception.GlobalExceptionHandling;
import com.example.doctorappointment.model.Doctor;
import com.example.doctorappointment.repository.AppointmentRepository;
import com.example.doctorappointment.repository.AppointmentSlotRepository;
import com.example.doctorappointment.repository.DoctorRepository;
import com.example.doctorappointment.repository.PatientRepository;
import com.example.doctorappointment.service.PatientService;
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

import java.time.format.DateTimeFormatter;

import static com.example.doctorappointment.util.AppointmentData.appointment;
import static com.example.doctorappointment.util.AppointmentSlotData.appointmentSlot;
import static com.example.doctorappointment.util.AppointmentSlotData.takenAppointmentSlot;
import static com.example.doctorappointment.util.DoctorData.doctor;
import static com.example.doctorappointment.util.PatientData.patientData;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentSlotRepository appointmentSlotRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    GlobalExceptionHandling exceptionHandler;
    private MockMvc mockMvc;

    private final static String PHONE_NUMBER = "09121234567";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setup() {
        PatientController controller = new PatientController(patientService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(jacksonMessageConverter)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(exceptionHandler)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        appointmentRepository.deleteAll();
        patientRepository.deleteAll();
        appointmentSlotRepository.deleteAll();
        doctorRepository.deleteAll();
    }

    @Test
    void getAppointments() throws Exception {
        Doctor doctor = doctorRepository.save(doctor());
        appointmentSlotRepository.save(appointmentSlot(doctor));
        patientRepository.save(patientData());
        appointmentRepository.save(appointment());

        mockMvc.perform(get("/api/v1/patient/appointments")
                        .param("phoneNumber", PHONE_NUMBER))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].appointmentSlot.startTime").value(takenAppointmentSlot().getStartTime().format(formatter)))
                .andExpect(jsonPath("$[0].appointmentSlot.endTime").value(takenAppointmentSlot().getEndTime().format(formatter)));

    }
}
