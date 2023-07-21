package com.example.doctorappointment.service.impl;

import com.example.doctorappointment.constant.Constant;
import com.example.doctorappointment.exception.CustomException;
import com.example.doctorappointment.model.User;
import com.example.doctorappointment.rabbitMq.ProducerService;
import com.example.doctorappointment.repository.UserRepository;
import com.example.doctorappointment.service.UserService;
import com.example.doctorappointment.rabbitMq.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProducerService producerService;
    @Override
    public User register(User user) {
        if (userRepository.existsUserByEmailAddressOrUserName(user.getEmailAddress(), user.getUserName()))
            throw new CustomException("this user already exist!", HttpStatus.CONFLICT);
        User newUser = userRepository.save(user);

        MessageDTO message = new MessageDTO()
                .emailAddress(newUser.getEmailAddress())
                .message(Constant.welcomeMessage);
        producerService.sendMessage(message);

        return newUser;
    }

    @Override
    public boolean authentication(String username, String password) {
        User user = this.findByUsername(username);
        return user.validation(password);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findUserByUserName(username).orElseThrow(
                () -> new CustomException("user doesn't exist!",HttpStatus.NOT_FOUND)
        );
    }
}
