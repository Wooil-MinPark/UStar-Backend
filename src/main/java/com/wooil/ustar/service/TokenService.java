package com.wooil.ustar.service;

import com.wooil.ustar.Util.jwt.JwtUtil;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public String refreshAccessToken(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken)) {
            String userId = jwtUtil.getUsernameFromToken(refreshToken);
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return jwtUtil.generateAccessToken(userId);
        }
        throw new RuntimeException("Invalid refresh token");
    }
}
