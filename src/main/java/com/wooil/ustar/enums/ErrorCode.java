package com.wooil.ustar.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    // [Global] 실패: 알수 없는 에러 (400)
    GLOBAL_001("unknown error"),

    // [Global] 실패: 서버 에러 (500)
    GLOBAL_002("server error"),

    // when username duplicated.
    USER_001("userName already exists"),

    // when user email duplicated.
    USER_002("userEmail already exists"),

    // invalid password when login
    USER_003("invalid password"),

    // user not found when login
    USER_004("user not found"),

    // invalid JWT token
    TOKEN_001("invalid JWT token"),

    // JWT token expired
    TOKEN_002("JWT token was expired"),
    ;

    private String message;
}
