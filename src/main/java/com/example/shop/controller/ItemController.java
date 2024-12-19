package com.example.shop.controller;

import com.example.shop.aop.PublicApi;
import com.example.shop.aop.SecurityAspect;
import com.example.shop.dto.item.ItemRequestDto;
import com.example.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<?> saveItem(@RequestPart("item")ItemRequestDto dto,
                                      @RequestPart("itemImages")List<MultipartFile> itemImages,
                                      @RequestPart("itemDetailImage") MultipartFile itemDetailImage) {
        itemService.saveItem(dto, itemImages, itemDetailImage);
        return ResponseEntity.status(HttpStatus.CREATED).body("상품이 성공적으로 생성되었습니다.");
    }

    @PublicApi
    @GetMapping
    public ResponseEntity<?> searchItems(@RequestParam(value = "category", required = false) String category,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getSearchItem(category, keyword, pageable));
    }

    @PublicApi
    @GetMapping("/{itemId}")
    public ResponseEntity<?> getItem(@PathVariable("itemId") Long itemId) {
        return ResponseEntity.status(HttpStatus.OK).body(itemService.getItem(itemId));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<?> updateItem(@PathVariable("itemId") Long itemId,
                                        @RequestPart("item")ItemRequestDto dto,
                                        @RequestPart("itemImages")List<MultipartFile> itemImages,
                                        @RequestPart("itemDetailImage")MultipartFile itemDetailImage) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        itemService.updateItem(itemId, dto, itemImages, itemDetailImage);
        return ResponseEntity.status(HttpStatus.OK).body("상품이 성공적으로 수정되었습니다.");
    }

    @PutMapping("/active/{itemId}")
    public ResponseEntity<?> activateItem(@PathVariable("itemId") Long itemId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        itemService.activateItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).body("상품이 성공적으로 활성화 되었습니다.");
    }

    @PutMapping("/inactive/{itemId}]")
    public ResponseEntity<?> inactiveItem(@PathVariable("itemId") Long itemId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        itemService.inActiveItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).body("상품이 성공적으로 비활성화 되었습니다.");
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable("itemId") Long itemId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        itemService.deleteItem(itemId);
        return ResponseEntity.status(HttpStatus.OK).body("상품이 성공적으로 삭제되었습니다.");
    }
}