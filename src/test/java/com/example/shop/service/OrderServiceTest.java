package com.example.shop.service;

import com.example.shop.domain.shop.Item;
import com.example.shop.domain.instagram.Member;
import com.example.shop.dto.*;
import com.example.shop.dto.item.ItemCategoryFormDto;
import com.example.shop.dto.item.ItemRequestDto;
import com.example.shop.dto.item.ItemSizeDto;
import com.example.shop.repository.ItemRepository;
import com.example.shop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    OrderService orderService;

    @Autowired
    ItemService itemService;

    @Autowired
    ItemCategoryService itemCategoryService;

    @Test
    @Rollback(value = false)
    void OrderTest() {
//        ItemCategoryFormDto dto1 = new ItemCategoryFormDto();
//        dto1.setName("EPL");
//        itemCategoryService.createItemCategory(dto1);
//
//        ItemCategoryFormDto dto2 = new ItemCategoryFormDto();
//        dto2.setName("Manchester United");
//        dto2.setParentCategory("EPL");
//        itemCategoryService.createItemCategory(dto2);
//
//        ItemCategoryFormDto dto3 = new ItemCategoryFormDto();
//        dto3.setName("Manchester City");
//        dto3.setParentCategory("EPL");
//        itemCategoryService.createItemCategory(dto3);
//
//        ItemSizeDto sizeDto1 = new ItemSizeDto();
//        sizeDto1.setSize("XS");
//        sizeDto1.setStockQuantity(200);
//
//        ItemSizeDto sizeDto2 = new ItemSizeDto();
//        sizeDto2.setSize("S");
//        sizeDto2.setStockQuantity(200);
//
//        ItemSizeDto sizeDto3 = new ItemSizeDto();
//        sizeDto3.setSize("M");
//        sizeDto3.setStockQuantity(200);
//
//        ItemSizeDto sizeDto4 = new ItemSizeDto();
//        sizeDto4.setSize("L");
//        sizeDto4.setStockQuantity(200);
//
//        ItemSizeDto sizeDto5 = new ItemSizeDto();
//        sizeDto5.setSize("XL");
//        sizeDto5.setStockQuantity(200);
//
//        ItemSizeDto sizeDto6 = new ItemSizeDto();
//        sizeDto6.setSize("2XL");
//        sizeDto6.setStockQuantity(200);
//
//        ItemRequestDto itemRequestDto1 = new ItemRequestDto();
//        itemRequestDto1.setItemCategory("Manchester United");
//        itemRequestDto1.setManufacturer("Adidas");
//        itemRequestDto1.setName("24-25 Home Replica");
//        itemRequestDto1.setPrice(150000);
//        itemRequestDto1.getItemSizes().add(sizeDto1);
//        itemRequestDto1.getItemSizes().add(sizeDto2);
//        itemRequestDto1.getItemSizes().add(sizeDto3);
//        itemRequestDto1.getItemSizes().add(sizeDto4);
//        itemRequestDto1.getItemSizes().add(sizeDto5);
//        itemRequestDto1.getItemSizes().add(sizeDto6);
//
//        itemService.saveItem(itemRequestDto1);
//
//        ItemRequestDto itemRequestDto2 = new ItemRequestDto();
//        itemRequestDto2.setItemCategory("Manchester City");
//        itemRequestDto2.setManufacturer("Puma");
//        itemRequestDto2.setName("24-25 Home Replica");
//        itemRequestDto2.setPrice(160000);
//        itemRequestDto2.getItemSizes().add(sizeDto1);
//        itemRequestDto2.getItemSizes().add(sizeDto2);
//        itemRequestDto2.getItemSizes().add(sizeDto3);
//        itemRequestDto2.getItemSizes().add(sizeDto4);
//        itemRequestDto2.getItemSizes().add(sizeDto5);
//        itemRequestDto2.getItemSizes().add(sizeDto6);
//
//        itemService.saveItem(itemRequestDto2);
//
//        MemberFormDto memberFormDto = new MemberFormDto();
//        memberFormDto.setName("Member");
//        memberService.saveMember(memberFormDto);
//
//        Member member = memberRepository.findByName("Member")
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));
//
//        OrderItemRequestDto orderItem1 = new OrderItemRequestDto();
//        OrderItemRequestDto orderItem2 = new OrderItemRequestDto();
//
//        Item item1 = itemRepository.findByCategoryNameAndItemName("Manchester United", "24-25 Home Replica")
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
//
//        Item item2 = itemRepository.findByCategoryNameAndItemName("Manchester City", "24-25 Home Replica")
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
//
//        orderItem1.setItemId(item1.getId());
//        orderItem1.setSize("M");
//        orderItem1.setCount(2);
//
//        orderItem2.setItemId(item2.getId());
//        orderItem2.setSize("XL");
//        orderItem2.setCount(1);
//
//        OrderRequestDto orderRequestDto = new OrderRequestDto();
//        orderRequestDto.getOrderItems().add(orderItem1);
//        orderRequestDto.getOrderItems().add(orderItem2);
//
//        orderService.saveOrder(member.getId(), orderRequestDto);
    }
}