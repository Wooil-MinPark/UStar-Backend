package com.wooil.ustar.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@EntityListeners(AuditingEntityListener.class)
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
    @Column(name = "user_registration_date", nullable = false)
    private LocalDate userRegistrationDate;

//    @CreatedDate
//    @Column(name = "created_at",nullable = false)
//    private LocalDateTime createdAt;
//
//    @LastModifiedDate
//    @Column(name = "updated_at",nullable = false)
//    private LocalDateTime updatedAt;

    public Long getUserUid() {
        return userUid;
    }

    public void setUserUid(Long userUid) {
        this.userUid = userUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public LocalDate getUserRegistrationDate() {
        return userRegistrationDate;
    }

    public void setUserRegistrationDate(LocalDate userRegistrationDate) {
        this.userRegistrationDate = userRegistrationDate;
    }
}
