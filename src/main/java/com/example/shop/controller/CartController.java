package com.example.shop.controller;

import com.example.shop.aop.SecurityAspect;
import com.example.shop.domain.shop.Order;
import com.example.shop.dto.cart.CartRequestDto;
import com.example.shop.dto.order.OrderRequestDto;
import com.example.shop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> saveOrUpdateCart(@RequestBody CartRequestDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();

        boolean isNewCart = cartService.saveOrUpdateCart(memberId, dto);

        if (isNewCart) {
            return ResponseEntity.status(HttpStatus.CREATED).body("장바구니에 상품이 추가되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("장바구니에 담긴 상품의 수량이 추가되었습니다.");
        }
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<?> updateCart(@PathVariable("cartId") Long cartId,
                                        @RequestParam int count) {

        Long memberId = SecurityAspect.getCurrentMemberId();

        if (count < 1) {
            throw new IllegalArgumentException("최소 수량은 1개입니다.");
        }

        cartService.updateCart(cartId, count);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니 항목의 수량이 업데이트되었습니다.");
    }

    @GetMapping
    public ResponseEntity<?> getCarts() {
        Long memberId = SecurityAspect.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(cartService.getCarts(memberId));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCart(@RequestParam List<Long> cartIds) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        cartService.deleteCartItems(memberId, cartIds);
        return ResponseEntity.status(HttpStatus.OK).body("선택한 상품들이 장바구니에서 제거되었습니다.");
    }

    @PostMapping("/orders")
    public ResponseEntity<?> saveOrderFromCart(@RequestBody OrderRequestDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        Order order = cartService.saveOrderFromCart(memberId, dto);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", order.getId());
        response.put("amount", order.getOrderPrice());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
