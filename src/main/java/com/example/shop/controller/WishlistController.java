package com.example.shop.controller;


import com.example.shop.domain.shop.WishlistItem;
import com.example.shop.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wishlists")
public class WishlistController {

    private final WishlistService wishlistService;

    // 특정 회원의 위시리스트를 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getWishlist(@PathVariable Long memberId) {
        return ResponseEntity.status(HttpStatus.OK).body
                (wishlistService.getWishlistByMemberId(memberId));
    }

    // 특정 회원의 위시리스트 저장 (장바구니 추가)
    @PostMapping("/{memberId}/save")
    public ResponseEntity<String> saveWishlist(@PathVariable Long memberId, @RequestBody WishlistItem wishlistItem) {
        wishlistService.saveWishlist(memberId, wishlistItem.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body("내 장바구니에 저장되었습니다.");
    }

    // 특정 회원의 위시리스트 삭제
    @DeleteMapping("/{memberId}/delete")
    public ResponseEntity<String> deleteWishlist(@RequestParam Long itemId) {
        wishlistService.deleteWishlist(itemId);
        return ResponseEntity.status(HttpStatus.OK).body("장바구니에 물건이 삭제되었습니다.");
    }
}
