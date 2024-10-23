package com.wooil.ustar.controller;

import com.wooil.ustar.dto.OrderDto;
import com.wooil.ustar.dto.UserDto;
import com.wooil.ustar.service.OrderService;
import com.wooil.ustar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    private OrderService orderService;

    @Autowired
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order/find")
    public List<OrderDto> findAllOrders() throws CloneNotSupportedException {

        return orderService.findAllOrders();
    }

    @PostMapping("/order/create")
    public void findUsersByUsername(@RequestBody Map<String, String> paramMap) {
        orderService.createOrder(Long.valueOf(paramMap.get("userId")), paramMap.get("product"));
    }

    @GetMapping("/order/find/{product}")
    public List<OrderDto> findAllOrdersByProduct(@PathVariable String product) {
        return orderService.findAllByProduct(product);
    }
}
