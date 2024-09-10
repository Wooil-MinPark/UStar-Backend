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

    @Column(name = "user_password", nullable = false,unique = true)
    private String userPassword;

    @CreatedDate
    @Column(name = "user_created_at",nullable = false)
    private LocalDateTime userCreatedAt;

    @LastModifiedDate
    @Column(name = "user_updated_at",nullable = false)
    private LocalDateTime userUpdatedAt;
}
