package com.wooil.ustar.service;

import com.wooil.ustar.domain.User;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import com.wooil.ustar.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserEmail(userEmail)
            .orElseThrow(() -> new CustomException(ErrorCode.LI_002,
                ErrorCode.LI_002.getMessage() + "user email: " + userEmail));

        return new org.springframework.security.core.userdetails.User(user.getUserEmail(),
            user.getUserPassword(), Collections.emptyList());
    }
}
