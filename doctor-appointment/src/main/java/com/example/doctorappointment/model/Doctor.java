package com.example.doctorappointment.model;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@EntityListeners(value = AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class Doctor implements Serializable {

    @Id
    @Column(name = "doctor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

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
