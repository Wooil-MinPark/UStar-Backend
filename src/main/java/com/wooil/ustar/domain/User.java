package com.wooil.ustar.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user", indexes = {
    @Index(name = "idx_user_email", columnList = "user_email")
})
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

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @CreatedDate
    @Column(name = "user_created_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime userCreatedAt;

    @LastModifiedDate
    @Column(name = "user_updated_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime userUpdatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> categories = new HashSet<>();

    // Builder 생성자를 재정의한 이유는 created_at와 updated_at가 자동 생성돼야하기 때문.
    @Builder
    public User(Long userUid, String userName, String userEmail, String userPassword) {
        this.userUid = userUid;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public void addCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("category cannot be null in User.addCategory");
        }
        categories.add(category);
        category.setUser(this);
    }

    public void removeCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("category cannot be null in User.removeCategory");
        }
        categories.remove(category);
        category.setUser(this);
    }

//    public Set<Category> getCategories() {
//        return Collections.unmodifiableSet(categories);
//    }
}
