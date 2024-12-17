package com.wooil.ustar.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "task")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_uid", nullable = false)
    private Long taskUid;

    @Column(name = "task_message", nullable = false)
    private String taskMessage;

    @Column(name = "task_time_duration", nullable = false)
    private Integer taskTimeDuration;

    @Column(name = "task_today_date", nullable = false)
    private LocalDate taskTodayDate;

    @CreatedDate
    @Column(name = "task_created_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskCreatedAt;

    @LastModifiedDate
    @Column(name = "task_updated_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskUpdatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_uid", nullable = false)
    private Category category;

    @Builder
    public Task(Long taskUid, String message, Integer timeDuration, LocalDate todayDate,
        Category category) {
        this.taskUid = taskUid;
        this.taskMessage = message;
        this.taskTimeDuration = timeDuration;
        this.taskTodayDate = todayDate;
        this.category = category;
    }
}
