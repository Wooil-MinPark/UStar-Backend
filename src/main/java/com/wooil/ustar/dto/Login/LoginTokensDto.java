package com.wooil.ustar.dto.Login;

import lombok.Builder;

@Builder
public record LoginTokensDto(String accessToken, String refreshToken) {
}
