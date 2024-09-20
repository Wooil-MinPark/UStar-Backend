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
        try{
            if (jwtUtil.validateToken(refreshToken)) {
                String userId = jwtUtil.getUsernameFromToken(refreshToken);
                User user = userRepository.findByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                return jwtUtil.generateAccessToken(userId);
            }
            throw new CustomException(ErrorCode.TK_001,ErrorCode.TK_001.getMessage());
        }catch (CustomException e){
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode(), e.getMessage());
        }
        catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.unknown, ErrorCode.unknown.getMessage());
        }

    }
}
