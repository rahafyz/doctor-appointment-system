package com.example.doctorappointment.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "patient")
@Getter
@Setter
@EntityListeners(value = AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Patient implements Serializable {

    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "patient")
    private List<Appointment> appointmentList;

    @Column(name = "created_date")
    @CreatedDate
    private Date createdDate;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

}
