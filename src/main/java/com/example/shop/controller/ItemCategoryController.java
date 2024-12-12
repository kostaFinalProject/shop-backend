package com.example.shop.controller;

import com.example.shop.aop.PublicApi;
import com.example.shop.dto.item.ItemCategoryFormDto;
import com.example.shop.service.ItemCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item-categories")
public class ItemCategoryController {

    private final ItemCategoryService itemCategoryService;

    @PostMapping
    public ResponseEntity<?> saveItemCategory(@RequestPart("itemCategory") ItemCategoryFormDto dto,
                                              @RequestPart("file") MultipartFile file) {
        itemCategoryService.saveItemCategory(dto, file);
        return ResponseEntity.status(HttpStatus.CREATED).body("상품 카테고리가 성공적으로 생성되었습니다.");
    }

    @PublicApi
    @GetMapping
    public ResponseEntity<?> getTopCategories() {
        return ResponseEntity.ok(itemCategoryService.getTopCategories());
    }

    @PublicApi
    @GetMapping("/{itemCategoryId}")
    public ResponseEntity<?> getChildrenCategories(@PathVariable("itemCategoryId") Long itemCategoryId) {
        return ResponseEntity.ok(itemCategoryService.getChildrenCategories(itemCategoryId));
    }

    @PutMapping("/{itemCategoryId}")
    public ResponseEntity<?> updateItemCategory(@PathVariable("itemCategoryId") Long itemCategoryId,
                                                @RequestPart("itemCategory") ItemCategoryFormDto dto,
                                                @RequestPart("file") MultipartFile file) {
        itemCategoryService.updateItemCategory(itemCategoryId, dto, file);
        return ResponseEntity.status(HttpStatus.OK).body("상품 카테고리가 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/{itemCategoryId}")
    public ResponseEntity<?> deleteItemCategory(@PathVariable("itemCategoryId") Long itemCategoryId) {
        itemCategoryService.deleteItemCategory(itemCategoryId);
        return ResponseEntity.status(HttpStatus.OK).body("상품 카테고리가 성공적으로 삭제되었습니다.");
    }
}
