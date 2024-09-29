package com.wooil.ustar.repository;

import java.util.Optional;
import com.wooil.ustar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserName(String userName);

    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByUserName(String userName);
}
