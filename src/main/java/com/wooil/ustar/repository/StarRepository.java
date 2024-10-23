package com.wooil.ustar.repository;

import com.wooil.ustar.domain.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Star, Long> {
    Star findByStarName(String starName);
}
