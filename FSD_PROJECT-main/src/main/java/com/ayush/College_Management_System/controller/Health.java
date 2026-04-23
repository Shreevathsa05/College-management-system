package com.ayush.College_Management_System.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/health")
public class Health {
    @GetMapping("")
    public String health(){
        return "ok";
    }
}
