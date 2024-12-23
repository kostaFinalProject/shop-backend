package com.example.shop.service;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.*;
import com.example.shop.dto.order.OrderItemResponseDto;
import com.example.shop.dto.order.OrderRequestDto;
import com.example.shop.dto.order.OrderResponseDto;
import com.example.shop.repository.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                    ItemSize itemSize = validationService.validateItemSizeById(orderItemRequestDto.getItemSizeId());
                    return OrderItem.createOrderItem(itemSize, itemSize.getItem().getPrice(),
                            orderItemRequestDto.getCount());
                })
                .toList();

        Order order = Order.createOrder(member, orderItems);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = validationService.validateOrderById(orderId);
        order.cancel();
    }

    @Transactional(readOnly = true)
    public Page<OrderResponseDto> getOrders(Long memberId, Pageable pageable) {
        Page<Order> orders = orderRepository.getOrdersByMember(memberId, pageable);

        List<OrderResponseDto> dtos = orders.getContent().stream()
                .map(order -> {
                    List<OrderItemResponseDto> orderItems = order.getOrderItems().stream()
                            .map(orderItem -> OrderItemResponseDto.createDto(
                                    orderItem.getItemSize().getItem().getId(),
                                    orderItem.getItemSize().getItem().getName(),
                                    orderItem.getItemSize().getSize().getSize(),
                                    orderItem.getItemPrice(),
                                    orderItem.getItemSize().getItem().getRepItemImage()
                            )).collect(Collectors.toList());

                    return OrderResponseDto.createDto(orderItems, order.getOrderPrice(), order.getOrderStatus().name());
                })
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, orders.getTotalElements());
    }

}
