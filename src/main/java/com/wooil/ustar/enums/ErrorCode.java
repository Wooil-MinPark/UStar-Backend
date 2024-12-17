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

    // JWT token authentication failed
    TOKEN_003("authentication failed"),

    // Cookie not found
    TOKEN_004("cookie not found"),

    /// refresh token not found in cookie
    TOKEN_005("token not found."),

    // Category not found
    CATEGORY_001("category not found"),

    // category: permission denied
    CATEGORY_002("permission denied this category"),

    // Task not found
    TASK_001("Task not found"),

    // Task: permission denied
    TASK_002("permission denied this task")
    ;


    private String message;
}
