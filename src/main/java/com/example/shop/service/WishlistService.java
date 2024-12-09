package com.example.shop.service;


import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.Item;
import com.example.shop.domain.shop.WishlistItem;
import com.example.shop.dto.item.WishlistResponseDto;
import com.example.shop.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishListRepository wishListRepository;
    private final ValidationService validationService;

    /** 관심상품 조회 */
    public List<WishlistResponseDto> getWishlistByMemberId(Long memberId) {
        List<WishlistItem> wishlist = wishListRepository.findByMemberId(memberId);

        return wishlist.stream()
                .map(wishlistItem -> new WishlistResponseDto(wishlistItem.getId(), wishlistItem.getItem().getName(),
                        wishlistItem.getItem().getPrice()))
                .collect(Collectors.toList());
    }

    /** 관심상품 등록 */
    public void saveWishlist (Long memberId, Long itemId){
        Member member = validationService.validateMemberById(memberId);
        Item item = validationService.validateItemById(itemId);

        WishlistItem wishlistItem = WishlistItem.createWishlistItem(member, item);
        wishListRepository.save(wishlistItem);
    }

    /** 관심상품 삭제 */
    public void deleteWishlist(Long wishListItemId){
        WishlistItem wishlistItem = wishListRepository.findById(wishListItemId)
                .orElseThrow(() -> new IllegalArgumentException("등록된 관심상품이 없습니다."));

        wishListRepository.delete(wishlistItem);
    }
}
