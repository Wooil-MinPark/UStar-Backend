package com.wooil.ustar.repository;

import com.wooil.ustar.domain.Task;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByCategoryCategoryUid(Long categoryUid);
    List<Task> findByTaskTodayDate(LocalDate todayDate);

    @Query("SELECT t FROM Task t JOIN FETCH t.category WHERE t.taskTodayDate = :date")
    List<Task> findByTodayDateWithCategory(@Param("date") LocalDate date);

    @Query("SELECT t FROM Task t JOIN FETCH t.category c WHERE  c.user.userUid = :userUid")
    List<Task> findAllByUserUid(@Param("userUid") Long userUid);
}
