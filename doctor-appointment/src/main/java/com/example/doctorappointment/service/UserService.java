package com.example.doctorappointment.service;

import com.example.doctorappointment.model.User;

public interface UserService {

    User save(User user);
    boolean authentication(String username, String password);
    String login(String username, String password);
    User findByUsername(String userName);

}
