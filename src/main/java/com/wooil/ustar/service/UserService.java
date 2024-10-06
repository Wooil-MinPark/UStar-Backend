package com.wooil.ustar.service;

import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.UserDto;
import com.wooil.ustar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {
    UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream().map(user->
            UserDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail()).build()
        ).toList();

        return userDtos;
    }
}
