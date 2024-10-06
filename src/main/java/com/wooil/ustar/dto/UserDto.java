package com.wooil.ustar.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDto {
    private Long userId;
    private String username;
    private String email;
}
