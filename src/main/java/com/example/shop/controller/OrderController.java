package com.example.shop.controller;

import com.example.shop.aop.SecurityAspect;
import com.example.shop.dto.order.OrderRequestDto;
import com.example.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> saveOrder(@RequestBody OrderRequestDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        orderService.saveOrder(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("주문이 등록되었습니다.");
    }

    @GetMapping
    public ResponseEntity<?> getOrdersByMember(@RequestParam(value = "page", defaultValue = "0") int page,
                                               @RequestParam(value = "size", defaultValue = "20") int size) {

        Long memberId = SecurityAspect.getCurrentMemberId();
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrders(memberId, pageable));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable("orderId") Long orderId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("주문이 취소되었습니다.");
    }
}