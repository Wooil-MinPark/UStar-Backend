package com.wooil.ustar.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "users")
@Table(name = "users", schema = "study")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "email")
    private String email;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

