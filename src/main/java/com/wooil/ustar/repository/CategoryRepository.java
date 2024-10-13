package com.wooil.ustar.repository;

import com.wooil.ustar.domain.Category;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryUid(Long categoryUid);

    @Query("SELECT c FROM Category c WHERE c.user.userEmail = :userEmail")
    Set<Category> findCategoriesByUserEmail(@Param("userEmail") String userEmail);
}
