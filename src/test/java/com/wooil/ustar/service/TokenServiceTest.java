package com.wooil.ustar.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
import org.junit.jupiter.api.Test;
import org.springframework.util.ReflectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private TokenService tokenService;

    private User user;
    private RefreshToken refreshToken;
    private final String TEST_EMAIL = "test@test.com";
    private final String TEST_USERNAME = "test";
    private final String TEST_PASSWORD = "password";
    private final String TEST_ACCESS_TOKEN = "test_access_token";
    private final String TEST_REFRESH_TOKEN = "test_refresh_token";


    @BeforeEach
    void setUp(){
        user = User.builder()
                .userUid(1L)
                .userEmail(TEST_EMAIL)
                .userName(TEST_USERNAME)
                .userPassword(TEST_PASSWORD)
                .build();

        refreshToken = RefreshToken.builder()
                .user(user)
                .tokenValue(TEST_REFRESH_TOKEN)
                .tokenExpiresAt(LocalDateTime.now().plusDays(7))
                .build();

        Field refreshTokenValidityField = ReflectionUtils.findField(TokenService.class, "refreshTokenValidity");
        refreshTokenValidityField.setAccessible(true);
        ReflectionUtils.setField(refreshTokenValidityField, tokenService, 604800000L);
    }

    @Test
    void refreshAccessToken_Success(){
        // given
        when(jwtUtil.validateToken(TEST_REFRESH_TOKEN)).thenReturn(true);
        when(jwtUtil.getUserEmailFromToken(TEST_REFRESH_TOKEN)).thenReturn(TEST_EMAIL);
        when(userRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(refreshToken));
        when(jwtUtil.generateAccessToken(TEST_EMAIL)).thenReturn(TEST_ACCESS_TOKEN);

        // when
        String newAccessToken = tokenService.refreshAccessToken(TEST_REFRESH_TOKEN);

        // then
        assertNotNull(newAccessToken);
        assertEquals(TEST_ACCESS_TOKEN, newAccessToken);
        verify(jwtUtil).validateToken(TEST_REFRESH_TOKEN);
        verify(jwtUtil).getUserEmailFromToken(TEST_REFRESH_TOKEN);
        verify(userRepository).findByUserEmail(TEST_EMAIL);
        verify(refreshTokenRepository).findByUser(user);
    }

    @Test
    void refreshAccessToken_TokenExpired(){
        // given
        when(jwtUtil.validateToken(TEST_REFRESH_TOKEN))
                .thenThrow(new CustomException(ErrorCode.TOKEN_002));

        // when
        CustomException exception = assertThrows(CustomException.class, () -> tokenService.refreshAccessToken(TEST_REFRESH_TOKEN));

        // then
        assertEquals(ErrorCode.TOKEN_002, exception.getErrorCode());
        verify(jwtUtil).validateToken(TEST_REFRESH_TOKEN);
        verifyNoMoreInteractions(userRepository, refreshTokenRepository);
    }

    @Test
    void refreshAccessToken_InvalidToken(){
        // given
        when(jwtUtil.validateToken(TEST_REFRESH_TOKEN))
                .thenThrow(new CustomException(ErrorCode.TOKEN_001));

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> tokenService.refreshAccessToken(TEST_REFRESH_TOKEN));

        // then
        assertEquals(ErrorCode.TOKEN_001, exception.getErrorCode());
        verify(jwtUtil).validateToken(TEST_REFRESH_TOKEN);
        verifyNoMoreInteractions(userRepository, refreshTokenRepository);
    }

    @Test
    void createRefreshToken_Success() {
        // given
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.empty());
        when(jwtUtil.generateRefreshToken(TEST_EMAIL)).thenReturn(TEST_REFRESH_TOKEN);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        // when
        RefreshToken result = tokenService.createRefreshToken(user);

        // then
        assertNotNull(result);
        assertEquals(TEST_REFRESH_TOKEN, result.getTokenValue());
        assertEquals(user, result.getUser());
        assertTrue(result.isValid());
        verify(jwtUtil).generateRefreshToken(TEST_EMAIL);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void createRefreshToken_DeleteExisting(){
        // given
        RefreshToken oldToken = RefreshToken.builder()
                .user(user)
                .tokenValue("old_refresh_token")
                .tokenExpiresAt(LocalDateTime.now().plusDays(7))
                .build();

        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(oldToken));
        when(jwtUtil.generateRefreshToken(TEST_EMAIL)).thenReturn(TEST_REFRESH_TOKEN);
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(refreshToken);

        // when
        RefreshToken result = tokenService.createRefreshToken(user);

        // then
        assertNotNull(result);
        assertEquals(TEST_REFRESH_TOKEN, result.getTokenValue());
        assertEquals(user, result.getUser());
        assertTrue(result.isValid());
        verify(jwtUtil).generateRefreshToken(TEST_EMAIL);
        verify(refreshTokenRepository).delete(oldToken);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void validateStoredRefreshToken_Success(){
        // given
        when(userRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(refreshToken));

        // when
        boolean result = tokenService.validateStoredRefreshToken(TEST_EMAIL,TEST_REFRESH_TOKEN);

        // then
        assertTrue(result);
        verify(userRepository).findByUserEmail(TEST_EMAIL);
        verify(refreshTokenRepository).findByUser(user);
    }

    @Test
    void validateStoredRefreshToken_ExpiredToken(){
        // given
        RefreshToken expiredRefreshToken = RefreshToken.builder()
                .user(user)
                .tokenValue(TEST_REFRESH_TOKEN)
                .tokenExpiresAt(LocalDateTime.now().minusDays(1))
                .build();
        when(userRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(expiredRefreshToken));

        // when
        boolean result = tokenService.validateStoredRefreshToken(TEST_EMAIL,TEST_REFRESH_TOKEN);

        // then
        assertFalse(result);
    }

    @Test
    void removeRefreshToken_Success(){
        // given
        when(userRepository.findByUserEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(refreshToken));

        // when
        tokenService.removeRefreshToken(TEST_EMAIL);

        // then
        verify(refreshTokenRepository).delete(refreshToken);
        assertNull(user.getRefreshToken());
    }

    @Test
    void findByUser_Success(){
        // given
        when(refreshTokenRepository.findByUser(user)).thenReturn(Optional.of(refreshToken));

        // when
        RefreshToken result = tokenService.findByUser(user);

        // then
        assertNotNull(result);
        assertEquals(TEST_REFRESH_TOKEN,result.getTokenValue());
        assertEquals(user, result.getUser());
        verify(refreshTokenRepository).findByUser(user);
    }

    @Test
    void extractRefreshTokenFromCookie_Success() {
        // given
        Cookie refreshTokenCookie = new Cookie(CookieName.REFRESH_TOKEN.getName(), TEST_REFRESH_TOKEN);
        Cookie[] cookies = new Cookie[]{refreshTokenCookie};
        when(request.getCookies()).thenReturn(cookies);

        // when
        String result = tokenService.extractRefreshTokenFromCookie(request);

        // then
        assertEquals(TEST_REFRESH_TOKEN, result);
    }
    @Test
    void extractRefreshTokenFromCookie_NullCookies() {
        // given
        when(request.getCookies()).thenReturn(null);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> tokenService.extractRefreshTokenFromCookie(request));

        // then
        assertEquals(ErrorCode.TOKEN_004, exception.getErrorCode());
    }

    @Test
    void extractRefreshTokenFromCookie_NoRefreshToken() {
        // given
        Cookie[] cookies = new Cookie[]{new Cookie("other_cookie", "value")};
        when(request.getCookies()).thenReturn(cookies);

        // when
        CustomException exception = assertThrows(CustomException.class,
                () -> tokenService.extractRefreshTokenFromCookie(request));

        // then
        assertEquals(ErrorCode.TOKEN_005, exception.getErrorCode());
    }


}
