package com.example.doctorappointment.service.impl;

import com.example.doctorappointment.exception.CustomException;
import com.example.doctorappointment.model.User;
import com.example.doctorappointment.repository.UserRepository;
import com.example.doctorappointment.security.JwtBuilder;
import com.example.doctorappointment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtBuilder jwtBuilder;


    @Override
    public User save(User user) {
        if (userRepository.existsUserByEmailAddressOrUserName(user.getEmailAddress(), user.getPassword()))
            throw new CustomException("this user already exist!", HttpStatus.CONFLICT);
        return userRepository.save(user);
    }

    @Override
    public boolean authentication(String username, String password) {
        User user = this.findByUsername(username);
        return user.validation(password);
    }

    @Override
    public String login(String username, String password) {
        if (!authentication(username, password))
            throw new CustomException("Invalid password", HttpStatus.UNPROCESSABLE_ENTITY);
        return jwtBuilder.generateToken(userRepository.findUserByUserName(username).get());
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findUserByUserName(username).orElseThrow(
                () -> new CustomException("user doesn't exist!", HttpStatus.NOT_FOUND)
        );
    }
}
