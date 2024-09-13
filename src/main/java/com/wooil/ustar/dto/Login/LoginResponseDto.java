package com.wooil.ustar.dto.Login;

import lombok.Builder;

@Builder
public record LoginResponseDto(String accessToken, String refreshToken) {
}
