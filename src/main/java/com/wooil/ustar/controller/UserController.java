package com.wooil.ustar.controller;

import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.Login.LoginRequestDto;
import com.wooil.ustar.dto.Login.LoginResponseDto;
import com.wooil.ustar.dto.Login.LoginTokensDto;
import com.wooil.ustar.dto.SignUpRequestDto;
import com.wooil.ustar.dto.response.APIResponse;
import com.wooil.ustar.dto.user.GetUserDto;
import com.wooil.ustar.dto.user.UpdateUserRequestDto;
import com.wooil.ustar.dto.user.UpdateUserResDto;
import com.wooil.ustar.dto.user.UserEmailCheckRequestDto;
import com.wooil.ustar.dto.user.UserNameCheckRequestDto;
import com.wooil.ustar.enums.CookieName;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.mapper.UserMapper;
import com.wooil.ustar.service.TokenService;
import com.wooil.ustar.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenService tokenService;

    @Value("${jwt.refresh-token-validity}")
    private long refreshTokenValidity;

    /*
     * [GET] check username duplicated
     * */
    @PostMapping("/signup/userNameDup")
    public ResponseEntity<APIResponse<Boolean>> checkUserNameDup(
        @RequestBody UserNameCheckRequestDto request) {
        APIResponse<Boolean> resp;
        try {
            boolean isDuplicated = userService.isUserNameDuplicated(request.getUserName());

            if (isDuplicated) {
                resp = new APIResponse<>(true, ErrorCode.USER_001,
                    ErrorCode.USER_001.getMessage(), true);
            } else {
                resp = new APIResponse<>(true, false);
            }
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during check userName duplicated", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    /*
     * [GET] check email duplicated
     * */
    @PostMapping("/signup/userEmailDup")
    public ResponseEntity<APIResponse<Boolean>> checkUserEmailDup(
        @RequestBody UserEmailCheckRequestDto request) {
        APIResponse<Boolean> resp;
        try {
            boolean isDuplicated = userService.isUserEmailDuplicated(request.getUserEmail());

            if (isDuplicated) {
                resp = new APIResponse<>(true, ErrorCode.USER_002,
                    ErrorCode.USER_002.getMessage(), true);
            } else {
                resp = new APIResponse<>(true, false);
            }
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during check userName duplicated", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }


    /*
     * [POST] 회원 가입 API
     * */
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<String>> signUpUser(
        @Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        APIResponse<String> resp;
        try {
            User user = userService.signUpUser(signUpRequestDto);
            resp = new APIResponse<>(true);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponseDto>> login(
        @RequestBody LoginRequestDto loginRequestDto,
        HttpServletResponse response
    ) {
        APIResponse<LoginResponseDto> resp;
        try {
            LoginTokensDto responseDto = userService.login(loginRequestDto);

            Cookie refreshTokenCookie = new Cookie(CookieName.REFRESH_TOKEN.getName(),
                responseDto.refreshToken());
                refreshTokenCookie.setHttpOnly(true);
                refreshTokenCookie.setSecure(false);
                refreshTokenCookie.setPath("/");
                refreshTokenCookie.setAttribute("SameSite", "Lax");
                refreshTokenCookie.setMaxAge(
                    (int) TimeUnit.MILLISECONDS.toSeconds(refreshTokenValidity));
                refreshTokenCookie.setDomain("localhost");

            response.addCookie(refreshTokenCookie);

            LoginResponseDto respDto = LoginResponseDto.builder()
                .accessToken(responseDto.accessToken())
                .build();

            resp = new APIResponse<>(true, respDto);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<APIResponse<UpdateUserResDto>> updateUser(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody UpdateUserRequestDto request) {
        APIResponse<UpdateUserResDto> resp;
        try {
            User user = userService.updateUser(userDetails, request);
            UpdateUserResDto resDto = UserMapper.user2UpdateUserResDto(user);
            resp = new APIResponse<>(true, resDto);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user information", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @GetMapping("/whoami")
    public ResponseEntity<APIResponse<GetUserDto>> getUser(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        APIResponse<GetUserDto> resp;
        try {
            GetUserDto user = userService.getUser(userDetails);
            resp = new APIResponse<>(true, user);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user information", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<APIResponse<Void>> deleteUser(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        APIResponse<Void> resp;
        try {
            userService.deleteUser(userDetails);
            resp = new APIResponse<>(true);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user information", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<APIResponse<Void>> logout(
        HttpServletResponse response,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        APIResponse<Void> resp;
        try {
            Cookie cookie = new Cookie(CookieName.REFRESH_TOKEN.getName(), null);
            cookie.setMaxAge(0);
            cookie.setPath("/api/auth");
            response.addCookie(cookie);

            tokenService.removeRefreshToken(userDetails.getUsername());

            resp = new APIResponse<>(true);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            final boolean isGlobalError = e.getErrorCode() == ErrorCode.GLOBAL_001
                || e.getErrorCode() == ErrorCode.GLOBAL_002;
            resp = new APIResponse<>(!isGlobalError, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user information", e);
            resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }
}
