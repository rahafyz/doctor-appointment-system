package com.example.doctorappointment.util;

import com.example.doctorappointment.dto.ReserveAppointmentDTO;

public class AppointmentFacadeData {
    private static final Long ID = 1L;
    public static ReserveAppointmentDTO reserveAppointmentDTO(){
        return ReserveAppointmentDTO.
                builder().appointmentSlotId(ID)
                .patientDTO(PatientData.createPatientDTO()).build();
    }
    public static ReserveAppointmentDTO reserveAppointmentDTO(Long appointmentSlotId){
        return ReserveAppointmentDTO.
                builder().appointmentSlotId(appointmentSlotId)
                .patientDTO(PatientData.createPatientDTO()).build();
    }
}
