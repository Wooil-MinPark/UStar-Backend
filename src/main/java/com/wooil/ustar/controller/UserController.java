package com.wooil.ustar.controller;

import com.wooil.ustar.dto.UserDto;
import com.wooil.ustar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/find")
    public List<UserDto> findAllUsers() {
        return userService.findAllUsers();
    }
}
