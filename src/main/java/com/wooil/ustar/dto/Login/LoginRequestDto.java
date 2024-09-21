package com.wooil.ustar.dto.Login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequestDto {
    private String userId;
    private String password;
}
