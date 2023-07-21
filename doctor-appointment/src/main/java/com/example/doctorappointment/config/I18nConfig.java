package com.example.doctorappointment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class I18nConfig {

    @Value("${blubank.default.language}")
    private String defaultLanguage;
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(new Locale(defaultLanguage));
        return localeResolver;
    }
    @Bean
    public void changeDefaultLocale() {
        LocaleContextHolder.setDefaultLocale(new Locale(defaultLanguage));
    }
}