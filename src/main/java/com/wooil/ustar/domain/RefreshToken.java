package com.wooil.ustar.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wooil.ustar.enums.ErrorCode;
import com.wooil.ustar.exception.CustomException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "refresh_token", indexes = {
    @Index(name = "idx_refresh_token_token_value", columnList = "tokenValue")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenUid;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", nullable = false)
    private User user;

    @Column(name = "token_value", nullable = false, unique = true)
    private String tokenValue;

    @CreatedDate
    @Column(name = "token_create_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tokenCreateAt;

    @LastModifiedDate
    @Column(name = "token_updated_at", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime tokenUpdatedAt;

    @Column(name = "token_expires_at", nullable = false)
    private LocalDateTime tokenExpiresAt;

    public boolean isValid() {
        return LocalDateTime.now().isBefore(tokenExpiresAt);
    }

    @Builder(builderClassName = "RefreshTokenBuilder")
    public static RefreshToken createRefreshToken(User user, String tokenValue, LocalDateTime tokenExpiresAt) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.tokenValue = tokenValue;
        refreshToken.tokenExpiresAt = tokenExpiresAt;
        refreshToken.setUser(user);
        return refreshToken;
    }

    private void setUser(User user) {
        if(user == null){
            throw new CustomException(ErrorCode.USER_004);
        }
        this.user = user;
        user.setRefreshToken(this);

    }
}
