package com.wooil.ustar.dto.Login;

import lombok.Builder;
import lombok.Getter;

@Builder
public record LoginResponseDto(String accessToken) {
}
