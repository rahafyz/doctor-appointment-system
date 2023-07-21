package com.example.doctorappointment.model;

import com.sun.istack.NotNull;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Entity
@Table(name = "user")
@Getter
@Setter
@EntityListeners(value = AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class User implements Serializable {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user-name")
    private String userName;

    @Column(name = "email_address", unique = true)
    @NotNull
    private String emailAddress;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column
    @Enumerated

    public boolean validation(String password){
        return this.password.equals(password);
    }
}
