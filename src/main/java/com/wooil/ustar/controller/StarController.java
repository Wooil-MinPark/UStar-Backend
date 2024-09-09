package com.wooil.ustar.controller;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StarController {

    @GetMapping("/")
    public String hello(){
        return "Hello";
    }
}
