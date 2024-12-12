package com.example.shop.controller;

import com.example.shop.dto.item.DiscountRequestDto;
import com.example.shop.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @PostMapping("/{itemId}")
    public ResponseEntity<?> saveDiscount(@PathVariable("itemId") Long itemId,
                                          @RequestBody DiscountRequestDto dto) {
        discountService.saveDiscount(itemId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("할인이 적용되었습니다.");
    }

    @DeleteMapping("/{discountId}")
    public ResponseEntity<?> deleteDiscount(@PathVariable("discountId") Long discountId) {
        discountService.deleteDiscount(discountId);
        return ResponseEntity.status(HttpStatus.OK).body("할인이 종료되었습니다.");
    }
}
