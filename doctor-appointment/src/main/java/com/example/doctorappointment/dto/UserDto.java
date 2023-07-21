package com.example.doctorappointment.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;

    private String userName;

//    @JsonIgnore
    private String password;

    private String emailAddress;

    private String role;

}
