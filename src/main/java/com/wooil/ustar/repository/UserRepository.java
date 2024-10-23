package com.wooil.ustar.repository;

import com.wooil.ustar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByUserName(String userName);
}
