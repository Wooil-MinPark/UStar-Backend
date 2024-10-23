package com.wooil.ustar.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Persistable;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "orders", schema = "study")
@IdClass(OrderPK.class)
public class Order implements Persistable<OrderPK>, Cloneable{

    @Override
    public Object clone() throws CloneNotSupportedException {
        Order cloned = (Order) super.clone();
        cloned.user = (User) user.clone();
        return cloned;
    }

    @Id
    @Column(name = "order_date")
    private String orderDate;

    @Column(name = "product")
    private String product;

    @Id
    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Order() {}

    @Builder
    public Order(String orderDate, String product, User user) {
        this.orderDate = orderDate;
        this.product = product;
        this.user = user;
    }

    public void update(String product) {
        this.product = product;
    }

    @Override
    public OrderPK getId() {
        return OrderPK.builder()
                .user(this.user)
                .orderDate(this.orderDate).build();
    }

    @Override
    public boolean isNew() {
        return this.orderDate == null;
    }


}
