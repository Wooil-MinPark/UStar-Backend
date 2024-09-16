package com.wooil.ustar.service;

import com.wooil.ustar.Util.jwt.JwtUtil;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.Login.LoginRequestDto;
import com.wooil.ustar.dto.Login.LoginResponseDto;
import com.wooil.ustar.dto.SignUpRequestDto;
import com.wooil.ustar.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        if (userRepository.existsByUserId(signUpRequestDto.getId())) {
            throw new RuntimeException("User ID already exists");
        }
        if (userRepository.existsByUserEmail(signUpRequestDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // 비밀번호 암호화 구현 예정
        User user = User.builder()
                .userId(signUpRequestDto.getId())
                .userName(signUpRequestDto.getName())
                .userEmail(signUpRequestDto.getEmail())
                .userPassword(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .build();

        return userRepository.save(user);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUserId(loginRequestDto.getUserId())
                .orElseThrow(() ->new RuntimeException("User not found"));

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
            throw new RuntimeException("Invalid password.");
        }

        // create jwt token when login success
        String accessToken = jwtUtil.generateAccessToken(user.getUserId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
