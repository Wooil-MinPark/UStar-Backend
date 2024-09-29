package com.wooil.ustar.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    // [Global] 실패: 알수 없는 에러 (400)
    unknown("unknown error"),

    // [Global] 실패: 서버 에러 (500)
    serverError("server error"),

    // [회원가입] 실패: user name 중복 시 (409)
    SU_001("userName already exists"),

    // [회원가입] 실패: email 중복 시 (409)
    SU_002("email already exists"),

    // [로그인] 실패: 비밀번호 불일치 (401)
    LI_001("invalid password"),

    // [로그인] 실패: 사용자 찾을 수 없음 (404)
    LI_002("user not found"),

    // [토큰] 실패: 유효하지 않는 refresh token
    TK_001("invalid refresh token"),
    ;
    ;
    private String message;
}
