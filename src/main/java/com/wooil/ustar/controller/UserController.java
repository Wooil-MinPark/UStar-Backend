package com.wooil.ustar.controller;

import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.Login.LoginRequestDto;
import com.wooil.ustar.dto.Login.LoginResponseDto;
import com.wooil.ustar.dto.SignUpRequestDto;
import com.wooil.ustar.dto.response.APIResponse;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    /*
     * [POST] 회원 가입 API
     * */
    @PostMapping("/signup")
    public ResponseEntity<APIResponse<String>> signUpUser(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        try {
            User user = userService.signUpUser(signUpRequestDto);
            APIResponse<String> resp = new APIResponse<>(true);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            APIResponse<String> resp = new APIResponse<>(false, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            APIResponse<String> resp = new APIResponse<>(false, ErrorCode.serverError, ErrorCode.serverError.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto responseDto = userService.login(loginRequestDto);
            APIResponse<LoginResponseDto> resp = new APIResponse<>(true,responseDto);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            APIResponse<LoginResponseDto> resp = new APIResponse<>(false,e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            APIResponse<LoginResponseDto> resp = new APIResponse<>(false, ErrorCode.serverError, ErrorCode.serverError.getMessage());
            return ResponseEntity.ok(resp);
        }
    }
}
