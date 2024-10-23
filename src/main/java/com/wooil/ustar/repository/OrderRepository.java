package com.wooil.ustar.repository;

import com.wooil.ustar.domain.Order;
import com.wooil.ustar.domain.OrderPK;
import com.wooil.ustar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, OrderPK> {
    List<Order> findAllByProduct(String product);
}
