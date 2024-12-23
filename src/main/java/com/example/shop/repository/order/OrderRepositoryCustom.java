package com.example.shop.repository.order;

import com.example.shop.domain.shop.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> getOrdersByMember(Long memberId, Pageable pageable);
}
