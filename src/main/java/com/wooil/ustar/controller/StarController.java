package com.wooil.ustar.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class StarController {
    @GetMapping("/")
    public String hello(){
        return "Hello!";
    }
}
