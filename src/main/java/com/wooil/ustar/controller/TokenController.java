package com.wooil.ustar.controller;

import com.wooil.ustar.dto.Token.AccessTokenResponse;
import com.wooil.ustar.dto.Token.RefreshTokenRequestDto;
import com.wooil.ustar.dto.response.APIResponse;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<APIResponse<AccessTokenResponse>> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        APIResponse<AccessTokenResponse> resp;
        try {
            String newAccessToken = tokenService.refreshAccessToken(refreshTokenRequestDto.getRefreshToken());
            resp = new APIResponse<>(true,new AccessTokenResponse(newAccessToken));
            return ResponseEntity.ok(resp);

        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001 || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        }catch (RuntimeException e){
            log.error("Unexpected error while fetching user information", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }
}
