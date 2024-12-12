package com.example.shop.service;

import com.example.shop.domain.shop.Discount;
import com.example.shop.domain.shop.Item;
import com.example.shop.dto.item.DiscountRequestDto;
import com.example.shop.repository.discount.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ValidationService validationService;

    @Transactional
    public void saveDiscount(Long itemId, DiscountRequestDto dto) {
        if (discountRepository.existsDiscountByItemId(itemId)) {
            throw new IllegalArgumentException("이미 할인이 진행중입니다.");
        }

        Item item = validationService.validateItemById(itemId);
        Discount discount = Discount.createDiscount(item, dto.getDiscountPercent());
        discountRepository.save(discount);
    }

    @Transactional
    public void updateDiscount(Long discountId, DiscountRequestDto dto) {
        if (dto.getDiscountPercent() < 1 || dto.getDiscountPercent() > 100) {
            throw new IllegalArgumentException("할인 비율은 0% ~ 100% 사이여야 합니다.");
        }

        Discount discount = validationService.validateDiscountById(discountId);
        discount.updateDiscount(dto.getDiscountPercent());
    }

    @Transactional
    public void deleteDiscount(Long discountId) {
        discountRepository.deleteById(discountId);
    }
}
