package com.wooil.ustar.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JWTConfig {
    private String secret;
    private long accessTokenValidity;
    private long refreshTokenValidity;
}
