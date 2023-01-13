package com.example.sayhi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/hi")
public class Hi {

    @GetMapping(value = "/{name}")
    public String hi(@PathVariable String name){
        return "hi "+name;
    }
}
