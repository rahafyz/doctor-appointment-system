package com.example.doctorappointment.security;

import com.example.doctorappointment.model.User;

public interface AuthenticationService {

    void sendVerificationCode(User user);

    String verification(String username, String verificationCode);
}
