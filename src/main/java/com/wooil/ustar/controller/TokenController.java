package com.wooil.ustar.controller;

import com.wooil.ustar.dto.Token.AccessTokenResponse;
import com.wooil.ustar.dto.Token.RefreshTokenRequestDto;
import com.wooil.ustar.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        try {
            String newAccessToken = tokenService.refreshAccessToken(refreshTokenRequestDto.getRefreshToken());
            return ResponseEntity.ok(new AccessTokenResponse(newAccessToken));
        }catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
