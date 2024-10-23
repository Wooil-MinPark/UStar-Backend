package com.wooil.ustar.service;

import com.wooil.ustar.domain.Order;
import com.wooil.ustar.domain.OrderPK;
import com.wooil.ustar.domain.User;
import com.wooil.ustar.dto.OrderDto;
import com.wooil.ustar.dto.UserDto;
import com.wooil.ustar.repository.OrderRepository;
import com.wooil.ustar.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class OrderService {
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    OrderService(OrderRepository orderRepository, UserRepository userRepository){
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<OrderDto> findAllOrders() throws CloneNotSupportedException {
        // Order 검색 후 객체 및 문자열 할당
        List<Order> orders = orderRepository.findAll();
        User user1 = (User) orders.get(0).getUser().clone();
        User user2 = ((Order) orders.get(0).clone()).getUser();
        User user3 = User.builder()
                .email(orders.get(0).getUser().getEmail())
                .userId(orders.get(0).getUser().getUserId())
                .userName(orders.get(0).getUser().getUserName())
                .build();

        String orderDate1 = orders.get(0).getOrderDate();
        String orderDate2 = orders.get(0).getOrderDate();
        String orderDate3 = orders.get(0).getOrderDate();

        // Order를 새로 조회한 후 값 변경
        Order foundOrder = orderRepository.findById(orders.get(0).getId()).get();
        foundOrder.getUser().setUserName("changed");
        foundOrder.setOrderDate("9999-99-99");

        // 메모리 해시값 확인
        int hash1 = System.identityHashCode(user1);
        int hash2 = System.identityHashCode(user2);
        int hash3 = System.identityHashCode(user3);
        int foundHash = System.identityHashCode(foundOrder.getUser());

        List<OrderDto> orderDtos = orders.stream().map(order->
            OrderDto.builder()
                .username(order.getUser().getUserName())
                .orderDate(order.getOrderDate())
                .product(order.getProduct()).build()
        ).toList();
        return orderDtos;
    }

    public List<OrderDto> findAllByProduct(String product) {
        List<Order> orders = orderRepository.findAllByProduct(product);
        List<OrderDto> orderDtos = orders.stream().map(order->
                OrderDto.builder()
                        .username(order.getUser().getUserName())
                        .orderDate(order.getOrderDate())
                        .product(order.getProduct()).build()
        ).toList();
        return orderDtos;
    }

    public void createOrder(Long userId, String product) {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String formattedDate = now.format(formatter);

        User user = userRepository.findById(userId).orElseThrow();

        Order order = Order.builder()
                .product(product)
                .user(user)
                .orderDate(formattedDate)
                .build();

        logger.info("주문자: {}", order.getId().getUser().getUserName());
    }

    public void processOrder(Order order) {
        if (order.isNew()) {
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
            String formattedDate = now.format(formatter);
            Order newOrder = Order.builder()
                    .orderDate(formattedDate)
                    .user(order.getUser())
                    .product(order.getProduct())
                    .build();
            orderRepository.save(newOrder);
        } else {
            order.update(order.getProduct());
        }
    }
}
