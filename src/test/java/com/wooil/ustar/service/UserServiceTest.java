package com.wooil.ustar.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wooil.ustar.Util.jwt.JwtUtil;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.Login.LoginRequestDto;
import com.wooil.ustar.dto.Login.LoginResponseDto;
import com.wooil.ustar.dto.SignUpRequestDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private UserService userService;

    private SignUpRequestDto signUpRequestDto;
    private LoginRequestDto loginRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        final String name = "testName";
        final String email = "test@test.com";
        final String password = "password";
        final String encodedPassword = "encodedPassword";

        signUpRequestDto = new SignUpRequestDto(name, email, password);
        loginRequestDto = new LoginRequestDto(email, password);
        user = User.builder()
            .userName(name)
            .userEmail(email)
            .userPassword(encodedPassword)
            .build();
    }

    @Test
    void signupUser_Success() {
        /*
        "when()" is set test environment
        * [for example]
        * when(userRepository.existsByUserId(anyString())).thenReturn(false);
        * > 1. when detected "userRepository.existsByUserId()" and entered anyString
        * > 2. then return false
        * */
        //
        //
        when(userRepository.existsByUserName(anyString())).thenReturn(false);
        when(userRepository.existsByUserEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.signUpUser(signUpRequestDto);

        /*
         * assertNotNull() checks value is not null.
         * assertEquals() checks whether the left value is equal to the right value.
         * assertTrue() checks if a given condition is true. If it's false, the test fails.
         * */
        assertNotNull(result);
        assertEquals("testName", result.getUserName());
        assertEquals("test@test.com", result.getUserEmail());
        assertEquals("encodedPassword", result.getUserPassword());

        /*
         * verify() confirms a specific method was called on a mock object.
         * It checks if an interaction occurred during the test.
         * */
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(signUpRequestDto.getPassword());
    }

    @Test
    void signUpUser_DuplicatedID() {
        when(userRepository.existsByUserName(anyString())).thenReturn(true);
        CustomException exception = assertThrows(CustomException.class,
            () -> userService.signUpUser(signUpRequestDto));

        assertEquals(ErrorCode.USER_001, exception.getErrorCode());
    }

    @Test
    void signUpUser_DuplicatedEmail() {
        when(userRepository.existsByUserEmail(anyString())).thenReturn(true);
        CustomException exception = assertThrows(CustomException.class,
            () -> userService.signUpUser(signUpRequestDto));

        assertEquals(ErrorCode.USER_002, exception.getErrorCode());
    }

    @Test
    void login_Success() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtUtil.generateAccessToken(anyString())).thenReturn("access_token");
        when(jwtUtil.generateRefreshToken(anyString())).thenReturn("refresh_token");
//        when(jwtUtil.validateToken(anyString())).thenReturn(true);

        LoginResponseDto res = userService.login(loginRequestDto);

        assertNotNull(res);
        assertEquals("access_token", res.accessToken());
        assertEquals("refresh_token", res.refreshToken());

        verify(userRepository).findByUserEmail(loginRequestDto.getUserEmail());
        verify(passwordEncoder).matches(loginRequestDto.getPassword(), user.getUserPassword());
        verify(jwtUtil).generateAccessToken(user.getUserEmail());
        verify(jwtUtil).generateRefreshToken(user.getUserEmail());
    }

    @Test
    void login_InvalidPassword() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class,
            () -> userService.login(loginRequestDto));
        assertEquals(ErrorCode.USER_003, exception.getErrorCode());
    }

    @Test
    void login_UserNotFound() {
        when(userRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
            () -> userService.login(loginRequestDto));

        assertEquals(ErrorCode.USER_004, exception.getErrorCode());
    }
}
