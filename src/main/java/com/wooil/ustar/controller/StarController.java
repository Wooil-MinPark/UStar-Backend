package com.wooil.ustar.controller;

import com.wooil.ustar.dto.UserDto;
import com.wooil.ustar.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StarController {
    @Autowired
    private StarService starService;

    @GetMapping("/star/get")
    public void getStar() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        starService.getStar();
    }
}
