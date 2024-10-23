package com.wooil.ustar.service;

import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.UserDto;
import com.wooil.ustar.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    private UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserDto> findAllUsers() {


        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream().map(user->
            UserDto.builder()
                .userId(user.getUserId())
                .username(user.getUserName())
                .email(user.getEmail()).build()
        ).toList();



        return userDtos;
    }

    public List<UserDto> findUsersByUserName(String username) {

        List<User> users = userRepository.findAllByUserName(username);
        List<UserDto> userDtos = users.stream().map(user->
                UserDto.builder()
                        .userId(user.getUserId())
                        .username(user.getUserName())
                        .email(user.getEmail()).build()
        ).toList();

        return userDtos;
    }
}
