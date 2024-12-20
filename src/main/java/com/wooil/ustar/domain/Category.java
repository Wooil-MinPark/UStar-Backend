package com.wooil.ustar.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_uid")
    private Long categoryUid;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "category_color")
    private String categoryColor;

    // TODO: n+1 문제 확인해보기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_uid", nullable = false)
    private User user;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Star> stars = new HashSet<>();

    // 연관관계 편의 메서드
    public void addStar(Star star) {
        if (star == null) {
            throw new IllegalArgumentException("star cannot be null in Category.addTask");
        }
        stars.add(star);
        star.setCategory(this);
    }

    public void removeStar(Star star) {
        if (star == null) {
            throw new IllegalArgumentException("star cannot be null in Category.removeTask");
        }
        stars.remove(star);
        star.setCategory(null);
    }
}
