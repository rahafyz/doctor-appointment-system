package com.example.doctorappointment.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "appointment")
@Getter
@Setter
@EntityListeners(value = AuditingEntityListener.class)
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Appointment implements Serializable {

    @Id
    @Column(name = "appointment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_slot_id")
    private AppointmentSlot appointmentSlot;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

}
