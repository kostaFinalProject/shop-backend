package com.example.shop.repository;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemCategoryRepository itemCategoryRepository;

    @Autowired
    SizeRepository sizeRepository;

    @Test
    void order() {
        Member member = Member.createMember("member");
        memberRepository.save(member);

        ItemCategory epl = ItemCategory.createItemCategory("EPL", null, null);
        ItemCategory manchesterUnited = ItemCategory.createItemCategory("Manchester United", epl, null);

        itemCategoryRepository.save(epl);
        itemCategoryRepository.save(manchesterUnited);

        Size xs = sizeRepository.findBySize("XS")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사이즈입니다."));
        Size s = sizeRepository.findBySize("S")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사이즈입니다."));
        Size m = sizeRepository.findBySize("M")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사이즈입니다."));
        Size l = sizeRepository.findBySize("L")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사이즈입니다."));
        Size xl = sizeRepository.findBySize("XL")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사이즈입니다."));
        Size txl = sizeRepository.findBySize("2XL")
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사이즈입니다."));

        ItemSize xsSize = ItemSize.createItemSize(xs, 200);
        ItemSize sSize = ItemSize.createItemSize(s, 200);
        ItemSize mSize = ItemSize.createItemSize(m, 200);
        ItemSize lSize = ItemSize.createItemSize(l, 200);
        ItemSize xlSize = ItemSize.createItemSize(xl, 200);
        ItemSize txlSize = ItemSize.createItemSize(txl, 200);

        List<ItemSize> sizes = new ArrayList<>();
        sizes.add(xsSize);
        sizes.add(sSize);
        sizes.add(mSize);
        sizes.add(lSize);
        sizes.add(xlSize);
        sizes.add(txlSize);

        Item uniform = Item.createItem(manchesterUnited, "Adidas", "24-25 Home Replica", 150000, sizes, null);
        itemRepository.save(uniform);


        OrderItem orderItem1 = OrderItem.createOrderItem(xlSize,  xlSize.getItem().getPrice(),2);

        OrderItem orderItem2 = OrderItem.createOrderItem(mSize,  mSize.getItem().getPrice(), 5);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        Order order = Order.createOrder(member, orderItems);
        orderRepository.save(order);

        order.cancel();
        orderRepository.delete(order);
    }

}