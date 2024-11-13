package com.wooil.ustar.service;

import com.wooil.ustar.Util.jwt.JwtUtil;
import com.wooil.ustar.domain.RefreshToken;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.enums.CookieName;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.repository.RefreshTokenRepository;
import com.wooil.ustar.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.swing.text.html.Option;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;


    public String refreshAccessToken(String refreshToken) {
        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new CustomException(ErrorCode.TOKEN_001);
            }
            String userEmail = jwtUtil.getUserEmailFromToken(refreshToken);

            User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            RefreshToken storedRefreshToken = refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_001));

            if (!storedRefreshToken.isValid() || !storedRefreshToken.getTokenValue()
                .equals(refreshToken)) {
                refreshTokenRepository.delete(storedRefreshToken);
                throw new CustomException(ErrorCode.TOKEN_003);
            }

            return jwtUtil.generateAccessToken(userEmail);

        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    // refresh token 생성 함수
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        try {
            // 기존 토큰 제거
            refreshTokenRepository.deleteByUser(user);

            // 새 refresh token 생성
            String refreshToken = jwtUtil.generateRefreshToken(user.getUserEmail());

            RefreshToken refreshTokenEntity = RefreshToken.builder()
                .user(user)
                .tokenValue(refreshToken)
                .tokenExpiresAt(LocalDateTime.now()
                    .plus(Duration.ofMillis(refreshTokenValidity)))
                .build();

            return refreshTokenRepository.save(refreshTokenEntity);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }


    // refresh token 제거함수
    public void removeRefreshToken(String userEmail) {
        try {
            User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            refreshTokenRepository.deleteByUser(user);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    // 저장돼있는 refresh token 검증 함수
    public boolean validateStoredRefreshToken(String userEmail, String refreshToken) {
        try {
            User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_004));

            return refreshTokenRepository.findByUser(user)
                .map(token -> token.isValid() && token.getTokenValue().equals(refreshToken))
                .orElse(false);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public RefreshToken findByUser(User user) {
        try {
            return refreshTokenRepository.findByUser(user)
                .orElseThrow(() -> new CustomException(ErrorCode.TOKEN_005));
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }
    }

    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        try {
            Cookie[] cookies = request.getCookies();

            if (cookies == null) {
                throw new CustomException(ErrorCode.TOKEN_004);
            }

            for (Cookie cookie : cookies) {
                if (CookieName.REFRESH_TOKEN.getName().equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
            throw new CustomException(ErrorCode.TOKEN_005);
        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.GLOBAL_001, e.getMessage());
        }

    }
}
