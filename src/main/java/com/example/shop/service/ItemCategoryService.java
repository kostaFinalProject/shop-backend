package com.example.shop.service;

import com.example.shop.domain.shop.ItemCategory;
import com.example.shop.domain.shop.ItemCategoryImg;
import com.example.shop.dto.item.ChildrenCategoryRequestDto;
import com.example.shop.dto.item.ItemCategoryFormDto;
import com.example.shop.dto.item.ParentCategoryRequestDto;
import com.example.shop.repository.itemcategory.ItemCategoryRepository;
import com.example.shop.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCategoryService {

    private final ItemCategoryRepository itemCategoryRepository;
    private final ImageService imageService;
    private final ValidationService validationService;

    @Transactional
    public void saveItemCategory(ItemCategoryFormDto dto, MultipartFile file) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("카테고리의 이름이 지정되지 않았습니다.");
        }

        ItemCategory itemCategory;

        if (dto.getParentCategory() != null && !dto.getParentCategory().isBlank()) {
            ItemCategory parentCategory = validationService.validateItemCategoryByName(dto.getParentCategory());

            ItemCategoryImg itemCategoryImg = imageService.saveItemCategoryImg(file);

            itemCategory = ItemCategory.createItemCategory(dto.getName(), parentCategory, itemCategoryImg);
            itemCategoryRepository.save(itemCategory);
        } else {
            itemCategory = ItemCategory.createItemCategory(dto.getName(), null, null);
            itemCategoryRepository.save(itemCategory);
        }
    }

    @Transactional
    public void updateItemCategory(Long itemCategoryId, ItemCategoryFormDto dto, MultipartFile file) {
        ItemCategory itemCategory = validationService.validateItemCategoryById(itemCategoryId);

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("카테고리의 이름이 지정되지 않았습니다.");
        }

        ItemCategory parentCategory = null;
        if (dto.getParentCategory() != null && !dto.getParentCategory().isBlank()) {
            parentCategory = validationService.validateItemCategoryByName(dto.getParentCategory());

            if (itemCategory.equals(parentCategory)) {
                throw new IllegalArgumentException("자기 자신을 상위 카테고리로 설정할 수 없습니다.");
            }
        }

        ItemCategoryImg itemCategoryImg = null;
        if (file != null && !file.isEmpty()) {
            itemCategoryImg = imageService.updateItemCategoryImg(itemCategory, file);
        }

        if (parentCategory == null) {
            if (itemCategoryImg != null) {
                throw new IllegalArgumentException("최상위 카테고리는 이미지를 가질 수 없습니다.");
            }
        }

        itemCategory.updateItemCategory(dto.getName(), parentCategory, itemCategoryImg);
    }

    @Transactional(readOnly = true)
    public List<ParentCategoryRequestDto> getTopCategories() {
        List<ItemCategory> categories = itemCategoryRepository.findCategoriesWithoutParentCategory();

        return categories.stream()
                .map(itemCategory ->
                        ParentCategoryRequestDto.createDto(itemCategory.getId(),
                                itemCategory.getName())).toList();
    }

    @Transactional(readOnly = true)
    public List<ChildrenCategoryRequestDto> getChildrenCategories(Long categoryId) {
        List<ItemCategory> categories = itemCategoryRepository.findCategoriesByParentCategoryId(categoryId);

        return categories.stream()
                .map(itemCategory ->
                        ChildrenCategoryRequestDto.createDto(itemCategory.getId(),
                        itemCategory.getName(), itemCategory.getItemCategoryImg().getImgUrl()))
                .toList();
    }

    @Transactional
    public void deleteItemCategory(Long itemCategoryId) {
        ItemCategory itemCategory = validationService.validateItemCategoryById(itemCategoryId);

        imageService.deleteItemCategoryImg(itemCategory);
        itemCategoryRepository.delete(itemCategory);
    }
}