package com.wooil.ustar.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CookieName {
    REFRESH_TOKEN("refresh_token"),
    ;
    private String name;
}
