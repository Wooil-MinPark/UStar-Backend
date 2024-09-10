package com.wooil.ustar.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_uid")
    private Long userUid;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false,unique = true)
    private String userEmail;

    @Column(name = "user_id", nullable = false,unique = true)
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @CreatedDate
    @Column(name = "user_created_at",nullable = false)
    private LocalDateTime userCreatedAt;

    @LastModifiedDate
    @Column(name = "user_updated_at",nullable = false)
    private LocalDateTime userUpdatedAt;

    // Builder 생성자를 재정의한 이유는 created_at와 updated_at가 자동 생성돼야하기 때문.
    @Builder
    public User(Long userUid, String userName, String userEmail, String userId, String userPassword) {
        this.userUid = userUid;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPassword = userPassword;
    }
}
