package com.wooil.ustar.domain;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
public class OrderPK implements Serializable {
    private User user;
    private String orderDate;

    public OrderPK() {
    }

    @Builder
    public OrderPK(User user, String orderDate) {
        this.user = user;
        this.orderDate = orderDate;
    }
}
