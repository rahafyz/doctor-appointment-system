package com.example.doctorappointment.security;

import com.example.doctorappointment.model.User;
import com.example.doctorappointment.rabbitMq.MessageDTO;
import com.example.doctorappointment.rabbitMq.ProducerService;
import com.example.doctorappointment.service.UserService;
import com.example.doctorappointment.redis.RedisService;
import com.example.doctorappointment.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final JwtBuilder jwtBuilder;
    private final ProducerService producerService;
    private final RedisService redisService;

    @Override
    public void sendVerificationCode(User user) {
        Random random = new Random();
        Integer code = random.nextInt(6);

        MessageDTO message = new MessageDTO()
                .emailAddress(user.getEmailAddress())
                .message(String.valueOf(code));
        producerService.sendMessage(message);

        redisService.setValue("auth-" + user.getUserName(), String.valueOf(code), TimeUnit.DAYS, 5);

    }

    @Override
    public String verification(String username, String verificationCode) {
        String code = redisService.getValue("auth-" + username);
        if (verificationCode.equals(code))
            return jwtBuilder.generateToken(userService.findByUsername(username));
        throw new CustomException("invalid code", HttpStatus.FORBIDDEN);
    }
}
