package com.wooil.ustar.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "star", schema = "study")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Star {

    @Id
    @Column(name="star_id")
    private Long starId;

    @Column(name="star_name")
    private String starName;

    @Column(name="star_brightness")
    private String starBrightness;

    public String getStarBrightness() {
        return this.starBrightness;
    }
}
