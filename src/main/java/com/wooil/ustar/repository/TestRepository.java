package com.wooil.ustar.repository;

import com.wooil.ustar.domain.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
