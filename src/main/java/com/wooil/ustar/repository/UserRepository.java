package com.wooil.ustar.repository;

import com.wooil.ustar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
