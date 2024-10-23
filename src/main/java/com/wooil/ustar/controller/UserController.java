package com.wooil.ustar.controller;

import com.wooil.ustar.dto.UserDto;
import com.wooil.ustar.service.UserService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/find")
    public List<UserDto> findAllUsers() {

        return userService.findAllUsers();
    }

    @PostMapping("/user/find/username")
    public List<UserDto> findUsersByUserName(@RequestBody Map<String, String> paramMap) {
        return userService.findUsersByUserName(paramMap.get("userName"));
    }
}
