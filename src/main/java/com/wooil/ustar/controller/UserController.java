package com.wooil.ustar.controller;

import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.Login.LoginRequestDto;
import com.wooil.ustar.dto.Login.LoginResponseDto;
import com.wooil.ustar.dto.SignUpRequestDto;
import com.wooil.ustar.dto.response.APIResponse;
import com.wooil.ustar.dto.user.GetUserDto;
import com.wooil.ustar.dto.user.UpdateUserDto;
import com.wooil.ustar.dto.user.UserEmailCheckRequestDto;
import com.wooil.ustar.dto.user.UserNameCheckRequestDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    /*
     * [GET] check username duplicated
     * */
    @PostMapping("/signup/userNameDup")
    public ResponseEntity<APIResponse<Boolean>> checkUserNameDup(
        @RequestBody UserNameCheckRequestDto request) {
        try {
            boolean isDuplicated = userService.isUserNameDuplicated(request.getUserName());
            APIResponse<Boolean> resp = new APIResponse<>(true,isDuplicated);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            APIResponse<Boolean> resp = new APIResponse<>(false, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during check userName duplicated", e);
            APIResponse<Boolean> resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
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
        try {
            boolean isDuplicated = userService.isUserEmailDuplicated(request.getUserEmail());
            APIResponse<Boolean> resp = new APIResponse<>(true,isDuplicated);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            APIResponse<Boolean> resp = new APIResponse<>(false, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during check userName duplicated", e);
            APIResponse<Boolean> resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
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
        try {
            User user = userService.signUpUser(signUpRequestDto);
            APIResponse<String> resp = new APIResponse<>(true);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            APIResponse<String> resp = new APIResponse<>(false, e.getErrorCode(), e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            APIResponse<String> resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<APIResponse<LoginResponseDto>> login(
        @RequestBody LoginRequestDto loginRequestDto) {
        try {
            LoginResponseDto responseDto = userService.login(loginRequestDto);
            APIResponse<LoginResponseDto> resp = new APIResponse<>(true, responseDto);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            APIResponse<LoginResponseDto> resp = new APIResponse<>(false, e.getErrorCode(),
                e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error during user registration", e);
            APIResponse<LoginResponseDto> resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<APIResponse<User>> updateUser(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @RequestBody UpdateUserDto updateUserDto) {
        User user = userService.updateUser(userDetails, updateUserDto);
        APIResponse<User> resp = new APIResponse<>(true);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/whoami")
    public ResponseEntity<APIResponse<GetUserDto>> getUser(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            GetUserDto user = userService.getUser(userDetails);
            APIResponse<GetUserDto> resp = new APIResponse<>(true, user);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            APIResponse<GetUserDto> resp = new APIResponse<>(false, e.getErrorCode(),
                e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user information", e);
            APIResponse<GetUserDto> resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<APIResponse<Void>> deleteUser(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            userService.deleteUser(userDetails);
            APIResponse<Void> resp = new APIResponse<>(true);
            return ResponseEntity.ok(resp);
        } catch (CustomException e) {
            APIResponse<Void> resp = new APIResponse<>(false, e.getErrorCode(),
                e.getMessage());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            log.error("Unexpected error while fetching user information", e);
            APIResponse<Void> resp = new APIResponse<>(false, ErrorCode.GLOBAL_002,
                e.getMessage());
            return ResponseEntity.ok(resp);
        }
    }
}
