package com.example.shop.service;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.Discount;
import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.WishListItem;
import com.example.shop.dto.wishlist.WishListItemResponseDto;
import com.example.shop.repository.wishlist.WishListItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListItemService {

    private final WishListItemRepository wishListItemRepository;
    private final ValidationService validationService;

    @Transactional
    public void saveWishListItem(Long memberId, Long itemId) {
        Member member = validationService.validateMemberById(memberId);
        Item item = validationService.validateItemById(itemId);

        if (wishListItemRepository.existsByMemberAndItem(member, item)) {
            throw new IllegalArgumentException("이미 관심 상품으로 등록했습니다.");
        }

        WishListItem wishListItem = WishListItem.createWishListItem(member, item);
        wishListItemRepository.save(wishListItem);
    }

    @Transactional
    public void deleteWishListItem(Long wishListItemId) {
        WishListItem wishListItem = validationService.validateWishListItemById(wishListItemId);
        wishListItemRepository.delete(wishListItem);
    }

    @Transactional(readOnly = true)
    public List<WishListItemResponseDto> getWishListItems(Long memberId) {
        List<WishListItem> wishListItems = wishListItemRepository.findWishListItemByMemberId(memberId);

        return wishListItems.stream()
                .map(wishListItem -> {
                    Discount discount = validationService.findDiscountByItemId(wishListItem.getItem().getId());
                    int price = wishListItem.getItem().getPrice();

                    if (discount != null) {
                        price = discount.getDiscountPrice();
                    }

                    return WishListItemResponseDto.createDto(wishListItem.getId(), wishListItem.getItem().getId(),
                            wishListItem.getItem().getName(), price,
                            wishListItem.getItem().getRepItemImage());
                })
                .toList();
    }
}
