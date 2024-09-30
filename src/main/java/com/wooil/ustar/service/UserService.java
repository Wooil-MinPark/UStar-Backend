package com.wooil.ustar.service;

import com.wooil.ustar.Util.jwt.JwtUtil;
import com.wooil.ustar.Util.userDetails.CustomUserDetails;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.Login.LoginRequestDto;
import com.wooil.ustar.dto.Login.LoginResponseDto;
import com.wooil.ustar.dto.SignUpRequestDto;
import com.wooil.ustar.dto.user.UpdateUserDto;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    // 회원가입 서비스
    // 아이디,이메일이 unique=true여서 이미 있는지 검사
    public User signUpUser(@NotNull SignUpRequestDto signUpRequestDto) {
        try {
            if (userRepository.existsByUserName(signUpRequestDto.getName())) {
                throw new CustomException(ErrorCode.USER_001, ErrorCode.USER_001.getMessage());
            }
            if (userRepository.existsByUserEmail(signUpRequestDto.getEmail())) {
                throw new CustomException(ErrorCode.USER_002, ErrorCode.USER_002.getMessage());
            }
            // 비밀번호 암호화 구현 예정
            User user = User.builder()
                .userName(signUpRequestDto.getName())
                .userEmail(signUpRequestDto.getEmail())
                .userPassword(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .build();

            return userRepository.save(user);

        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.unknown, ErrorCode.unknown.getMessage());
        }
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            User user = userRepository.findByUserEmail(loginRequestDto.getUserEmail())
                .orElseThrow(
                    () -> new CustomException(ErrorCode.USER_004, ErrorCode.USER_004.getMessage()));

            // User Not found error
            if (user == null) {
                //  추후에 개선
                return LoginResponseDto.builder()
                    .accessToken("")
                    .refreshToken("")
                    .build();
            }

            // unauthorized error
            if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getUserPassword())) {
                //  추후에 개선
                throw new CustomException(ErrorCode.USER_003, ErrorCode.USER_003.getMessage());
            }

            // create jwt token when login success
            String accessToken = jwtUtil.generateAccessToken(user.getUserEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUserEmail());

            return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        } catch (CustomException e) {
            log.error(e.getMessage());
            throw new CustomException(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.unknown, ErrorCode.unknown.getMessage());
        }
    }

    // check userName is duplicated
    public boolean isUserNameDuplicated(String userName) {
        try {
            return userRepository.existsByUserName(userName);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.unknown, ErrorCode.unknown.getMessage());
        }
    }

    // check userEmail is duplicated
    public boolean isUserEmailDuplicated(String email) {
        try {
            return userRepository.existsByUserEmail(email);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.unknown, ErrorCode.unknown.getMessage());
        }
    }


    public User updateUser(CustomUserDetails userDetails, UpdateUserDto updateUserDto) {
        try {
            User user = userRepository.findByUserEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_004, ErrorCode.USER_004.getMessage()));

            if (updateUserDto.getName() != null && !user.getUserName()
                .equals(updateUserDto.getName()) && userRepository.existsByUserName(
                updateUserDto.getName())) {
                throw new CustomException(ErrorCode.USER_001, ErrorCode.USER_001.getMessage());
            }

            if(updateUserDto.getName() != null){
                user.setUserName(updateUserDto.getName());
            }

            return userRepository.save(user);

        } catch (CustomException e) {
            log.error(e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(ErrorCode.unknown, ErrorCode.unknown.getMessage());
        }
    }
}
