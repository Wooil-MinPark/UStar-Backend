package com.wooil.ustar.repository;

import com.wooil.ustar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserId(String userId);

    boolean existsByUserEmail(String userEmail);

    Optional<User> findByUserId(String userId);
}
