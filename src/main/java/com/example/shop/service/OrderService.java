package com.example.shop.service;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.ItemSize;
import com.example.shop.domain.shop.Order;
import com.example.shop.domain.shop.OrderItem;
import com.example.shop.dto.order.OrderRequestDto;
import com.example.shop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ValidationService validationService;

    @Transactional
    public void saveOrder(Long memberId, OrderRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);

        List<OrderItem> orderItems = dto.getOrderItems().stream()
                .map(orderItemRequestDto -> {
                    Item item = validationService.validateItemById(orderItemRequestDto.getItemId());
                    ItemSize itemSize = item.getItemSizeBySize(orderItemRequestDto.getSize());
                    return OrderItem.createOrderItem(itemSize, item.getPrice(), orderItemRequestDto.getCount());
                })
                .toList();

        Order order = Order.createOrder(member, orderItems);
        orderRepository.save(order);
    }
}
