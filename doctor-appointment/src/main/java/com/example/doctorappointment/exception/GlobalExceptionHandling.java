package com.example.doctorappointment.exception;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandling {

    private final MessageSource messageSource;

    public GlobalExceptionHandling(MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto> handleCustomException(CustomException ex) {
        ResponseDto response = ResponseDto.builder()
                .status(ex.getHttpStatus().value())
                .message(messageSource.getMessage(ex.getMessage(), null, LocaleContextHolder.getLocale()))
                .error(ex.getHttpStatus().getReasonPhrase())
                .timestamp(new Date())
                .build();
        return new ResponseEntity<>(response, ex.getHttpStatus());
    }

}