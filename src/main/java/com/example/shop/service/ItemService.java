package com.example.shop.service;

import com.example.shop.domain.shop.*;
import com.example.shop.dto.item.ItemRequestDto;
import com.example.shop.dto.item.ItemSizeDto;
import com.example.shop.dto.item.ItemSummaryResponseDto;
import com.example.shop.repository.item.ItemRepository;
import com.example.shop.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ImageService itemImgService;
    private final ValidationService validationService;

    /** 상품 등록 */
    @Transactional
    public void saveItem(ItemRequestDto dto, List<MultipartFile> itemImages) {
        ItemCategory itemCategory = validationService.validateItemCategoryByName(dto.getItemCategory());

        if (validationService.existItemName(dto.getName())) {
            throw new IllegalArgumentException("이미 동일한 이름의 상품이 존재합니다.");
        }

        List<ItemSize> itemSizes = dto.getItemSizes().stream()
                .map(itemSizeDto -> {
                    Size size = validationService.validateSizeBySize(itemSizeDto.getSize());
                    return ItemSize.createItemSize(size, itemSizeDto.getStockQuantity());
                })
                .toList();

        List<ItemImg> itemImgs = itemImgService.saveItemImgs(itemImages);

        Item item = Item.createItem(itemCategory, dto.getManufacturer(), dto.getName(), dto.getSeller(), dto.getPrice(), itemSizes, itemImgs);
        itemRepository.save(item);
    }

    /** 상품 수정 */
    @Transactional
    public void updateItem(Long itemId, ItemRequestDto dto, List<MultipartFile> itemImages) {
        Item item = validationService.validateItemById(itemId);
        ItemCategory itemCategory = validationService.validateItemCategoryByName(dto.getItemCategory());

        if (validationService.existItemNameExceptSelf(dto.getName(), itemId)) {
            throw new IllegalArgumentException("이미 동일한 이름의 상품이 존재합니다.");
        }

        for (ItemSizeDto sizeDto : dto.getItemSizes()) {
            ItemSize itemSize = item.getItemSizeBySize(sizeDto.getSize());
            itemSize.updateStockQuantity(sizeDto.getStockQuantity());
        }

        List<ItemImg> newItemImages = itemImgService.updateItemImgs(item, itemImages);

        item.updateItem(itemCategory, dto.getName(), dto.getPrice(), dto.getManufacturer());
        item.updateItemImg(newItemImages);
    }

    /** 상품 조회 활성화 */
    @Transactional
    public void activateItem(Long itemId) {
        Item item = validationService.validateItemById(itemId);
        item.activeItem();
    }

    /** 상품 조회 비활성화 */
    @Transactional
    public void inActiveItem(Long itemId) {
        Item item = validationService.validateItemById(itemId);
        item.inActiveItem();
    }

    /** 상품 삭제 */
    @Transactional
    public void deleteItem(Long itemId) {
        Item item = validationService.validateItemById(itemId);

        for (ItemSize itemSize : item.getItemSizes()) {
            itemSize.removeStock(itemSize.getStockQuantity());
        }

        itemImgService.deleteItemImg(item);
        item.deleteItem();
    }

    /** 상품 통합 조회 */
    @Transactional(readOnly = true)
    public Page<ItemSummaryResponseDto> getSearchItem(String category, String keyword, Pageable pageable) {
        Page<Item> items = itemRepository.searchItems(category, keyword, pageable);

        List<ItemSummaryResponseDto> dtos = items.stream()
                .map(item -> ItemSummaryResponseDto.createDto(item.getId(), item.getItemCategory().getName(), item.getManufacturer(), item.getName(),
                        item.getPrice(), item.getRepItemImage(), item.getItemStatus().toString(), item.getSeller()))
                .toList();

        return new PageImpl<>(dtos, pageable, items.getTotalElements());
    }
}