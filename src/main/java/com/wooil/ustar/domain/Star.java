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
@Table(name = "star")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Star {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "star_uid", nullable = false)
    private Long starUid;

    @Column(name = "star_message", nullable = false)
    private String starMessage;

    @Column(name = "star_time_duration", nullable = false)
    private Integer starTimeDuration;

    @Column(name = "star_today_date", nullable = false)
    private LocalDate starTodayDate;

    @Column(name = "star_coordinate_x", nullable = false)
    private Integer starCoordinateX;

    @Column(name = "star_coordinate_y", nullable = false)
    private Integer starCoordinateY;

    @CreatedDate
    @Column(name = "star_created_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime starCreatedAt;

    @LastModifiedDate
    @Column(name = "star_updated_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime starUpdatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_uid", nullable = false)
    private Category category;


    @Builder
    public Star(Long starUid, String starMessage, Integer starTimeDuration, LocalDate starTodayDate,
        Category category,Integer starCoordinateX, Integer starCoordinateY) {
        this.starUid = starUid;
        this.starMessage = starMessage;
        this.starTimeDuration = starTimeDuration;
        this.starTodayDate = starTodayDate;
        this.category = category;
        this.starCoordinateX = starCoordinateX;
        this.starCoordinateY = starCoordinateY;
    }
}
