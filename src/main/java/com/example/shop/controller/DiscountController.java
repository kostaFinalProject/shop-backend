package com.example.shop.controller;

import com.example.shop.domain.shop.Discount;
import com.example.shop.dto.item.DiscountRequestDto;
import com.example.shop.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/{itemId}")
    public ResponseEntity<?> saveDiscount(@PathVariable("itemId") Long itemId,
                                          @RequestBody DiscountRequestDto dto) {
        Discount discount = discountService.saveDiscount(itemId, dto);
        Map<String, Object> response = new HashMap<>();
        response.put("discountId", discount.getId());
        response.put("discountPercent", discount.getDiscountPercent());
        response.put("discountPrice", discount.getDiscountPrice());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{discountId}")
    public ResponseEntity<?> deleteDiscount(@PathVariable("discountId") Long discountId) {
        discountService.deleteDiscount(discountId);
        return ResponseEntity.status(HttpStatus.OK).body("할인이 종료되었습니다.");
    }
}
