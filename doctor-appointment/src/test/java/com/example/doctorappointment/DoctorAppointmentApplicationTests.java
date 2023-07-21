package com.example.doctorappointment;

import com.example.doctorappointment.config.EmbeddedRedisConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {EmbeddedRedisConfiguration.class})
class DoctorAppointmentApplicationTests {

	@Test
	void contextLoads() {
	}

}
