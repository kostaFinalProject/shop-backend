package com.example.shop.service;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.*;
import com.example.shop.dto.item.*;
import com.example.shop.repository.discount.DiscountRepository;
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
    private final DiscountRepository discountRepository;
    private final ImageService imageService;
    private final ValidationService validationService;

    /** 상품 등록 */
    @Transactional
    public void saveItem(ItemRequestDto dto, List<MultipartFile> itemImages, MultipartFile itemDetailImage) {
        ItemCategory itemCategory = validationService.validateItemCategoryByName(dto.getItemCategory());

        if (validationService.existItemNameInItemCategory(dto.getItemCategory(), dto.getName())) {
            throw new IllegalArgumentException("이미 동일한 이름의 상품이 존재합니다.");
        }

        List<ItemSize> itemSizes = dto.getItemSizes().stream()
                .map(itemSizeDto -> {
                    Size size = validationService.validateSizeBySize(itemSizeDto.getSize());
                    return ItemSize.createItemSize(size, itemSizeDto.getStockQuantity());
                })
                .toList();

        List<ItemImg> itemImgs = imageService.saveItemImgs(itemImages);

        ItemDetailImg itemDetailImg = imageService.saveItemDetailImg(itemDetailImage);

        Item item = Item.createItem(itemCategory, dto.getManufacturer(), dto.getName(),
                dto.getSeller(), dto.getPrice(), itemSizes, itemImgs, itemDetailImg);
        itemRepository.save(item);
    }

    /** 상품 수정 */
    @Transactional
    public void updateItem(Long itemId, ItemRequestDto dto, List<MultipartFile> itemImages, MultipartFile itemDetailImage) {
        Item item = validationService.validateItemById(itemId);
        ItemCategory itemCategory = validationService.validateItemCategoryByName(dto.getItemCategory());

        if (validationService.existItemNameInItemCategoryExceptSelf(dto.getItemCategory(), dto.getName(), itemId)) {
            throw new IllegalArgumentException("이미 동일한 이름의 상품이 존재합니다.");
        }

        for (ItemSizeDto sizeDto : dto.getItemSizes()) {
            ItemSize itemSize = item.getItemSizeBySize(sizeDto.getSize());
            itemSize.updateStockQuantity(sizeDto.getStockQuantity());
        }

        List<ItemImg> newItemImages = imageService.updateItemImgs(item, itemImages);
        ItemDetailImg newItemDetailImg = imageService.saveItemDetailImg(itemDetailImage);

        item.updateItem(itemCategory, dto.getName(), dto.getPrice(), dto.getManufacturer(), dto.getSeller(), newItemDetailImg);
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

        item.deleteItem();
    }

    /** 상품 통합 조회 */
    @Transactional(readOnly = true)
    public Page<ItemSummaryResponseDto> getSearchItem(String category, String keyword, Pageable pageable) {
        Page<Item> items = itemRepository.searchItems(category, keyword, pageable);

        List<ItemSummaryResponseDto> dtos = items.stream()
                .map(item -> {

                    int discountPercent = 0;
                    int discountPrice = item.getPrice();
                    if (discountRepository.existsDiscountByItemId(item.getId())) {
                        Discount discount = discountRepository.findDiscountByItemId(item.getId())
                                .orElseThrow(() -> new IllegalArgumentException("할인 진행중이 아닙니다."));

                        discountPercent = discount.getDiscountPercent();
                        discountPrice = discount.getDiscountPrice();
                    }

                    return ItemSummaryResponseDto.createDto(item.getId(), item.getItemCategory().getName(),
                            item.getManufacturer(), item.getName(), item.getPrice(), item.getRepItemImage(),
                            item.getItemStatus().toString(), item.getSeller(), discountPercent, discountPrice);
                })
                .toList();

        return new PageImpl<>(dtos, pageable, items.getTotalElements());
    }

    /** 상품 단건 조회 */
    @Transactional(readOnly = true)
    public ItemDetailResponseDto getItem(Long memberId, Long itemId) {
        Member member;
        String memberGrade = null;
        if (memberId != null) {
            member = validationService.validateMemberById(memberId);
            memberGrade = member.getGrade().name();
        }

        System.out.println("memberGrade = " + memberGrade);
        Item item = validationService.findItemById(itemId);

        List<ItemSizeResponseDto> itemSizesDto = item.getItemSizes().stream()
                .map(itemSize -> ItemSizeResponseDto.createDto(itemSize.getId(),
                        itemSize.getSize().getSize(), itemSize.getStockQuantity()))
                .toList();

        List<String> itemImageUrls = item.getItemImages().stream()
                .map(ItemImg::getImgUrl)
                .toList();

        Long discountId= null;
        int discountPercent = 0;
        int discountPrice = item.getPrice();
        if (discountRepository.existsDiscountByItemId(itemId)) {
            Discount discount = discountRepository.findDiscountByItemId(item.getId())
                    .orElseThrow(() -> new IllegalArgumentException("할인 진행중이 아닙니다."));

            discountId = discount.getId();
            discountPercent = discount.getDiscountPercent();
            discountPrice = discount.getDiscountPrice();
        }

        return ItemDetailResponseDto.createDto(itemId, discountId, item.getItemCategory().getParentItemCategory().getName(),
                item.getItemCategory().getName(), item.getManufacturer(),
                item.getName(), item.getPrice(), item.getSeller(), itemSizesDto, itemImageUrls,
                item.getItemDetailImg().getImgUrl(), discountPercent, discountPrice, memberGrade);
    }
}