package com.wooil.ustar.repository;

import com.wooil.ustar.domain.RefreshToken;
import com.wooil.ustar.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByTokenValue(String tokenValue);

    void deleteByUser(User user);
    boolean existsByTokenValue(String tokenValue);
}
