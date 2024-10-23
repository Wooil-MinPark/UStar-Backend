package com.wooil.ustar.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class OrderDto {
    private String username;
    private String orderDate;
    private String product;
}
