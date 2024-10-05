package com.wooil.ustar.service;

import com.wooil.ustar.Util.jwt.JwtUtil;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public String refreshAccessToken(String refreshToken) {
        try {
            if (jwtUtil.validateToken(refreshToken)) {
                String userEmail = jwtUtil.getUsernameFromToken(refreshToken);
                /// 여기 에러코드 추가
                User user = userRepository.findByUserEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
                return jwtUtil.generateAccessToken(userEmail);
            }
            throw new CustomException(ErrorCode.TOKEN_001);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }

    }
}
