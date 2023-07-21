package com.example.doctorappointment.security;

import com.example.doctorappointment.dto.UserDto;
import com.example.doctorappointment.exception.CustomException;
import com.example.doctorappointment.mapper.UserMapper;
import com.example.doctorappointment.model.User;
import com.example.doctorappointment.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthentiocationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestParam String username, @RequestParam String password) {
        if (!userService.authentication(username, password))
            throw new CustomException("Invalid password", HttpStatus.UNPROCESSABLE_ENTITY);
        User user = userService.findByUsername(username);
        authenticationService.sendVerificationCode(user);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @PostMapping("/verification/{verificationCode}")
    public ResponseEntity<String> verification(@RequestParam String username, @PathVariable String verificationCode) {
        return ResponseEntity.ok(authenticationService.verification(username, verificationCode));
    }
}
