package com.wooil.ustar.repository;

import com.wooil.ustar.domain.Star;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StarRepository extends JpaRepository<Star, Long> {

    List<Star> findByCategoryCategoryUid(Long categoryUid);

    List<Star> findByStarTodayDate(LocalDate todayDate);

    @Query("SELECT t FROM Star t JOIN FETCH t.category WHERE t.starTodayDate = :date")
    List<Star> findByTodayDateWithCategory(@Param("date") LocalDate date);

    @Query("SELECT t FROM Star t JOIN FETCH t.category c WHERE  c.user.userUid = :userUid")
    List<Star> findAllByUserUid(@Param("userUid") Long userUid);
}
