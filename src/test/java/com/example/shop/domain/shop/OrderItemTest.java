package com.example.shop.domain.shop;

import com.example.shop.domain.instagram.Member;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Domain 기능 Test (DB 사용 X)
 */
class OrderItemTest {

    @Test
    void orderItem_ItemSize() {
        //given
        ItemCategory epl = ItemCategory.createItemCategory("EPL", null, null);
        ItemCategory manchesterUnited = ItemCategory.createItemCategory("Manchester United", epl, null);

        Size xs = Size.createSize("XS");
        Size s = Size.createSize("S");
        Size m = Size.createSize("M");
        Size l = Size.createSize("L");
        Size xl = Size.createSize("XL");
        Size txl = Size.createSize("2XL");

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

        //when
        Member member = Member.createMember("member");

        OrderItem orderItem1 = OrderItem.createOrderItem(xlSize, xlSize.getItem().getPrice(),2);

        OrderItem orderItem2 = OrderItem.createOrderItem(mSize, mSize.getItem().getPrice(), 5);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);

        Order order = Order.createOrder(member, orderItems);

        //then
        assertThat(uniform.getAllStockQuantity()).isEqualTo(1193);
        assertThat(orderItem1.getItemSize()).isEqualTo(xlSize);
        assertThat(order.getOrderItems().get(0).getTotalPrice()).isEqualTo(300000);
        assertThat(order.getOrderPrice()).isEqualTo(1050000);
    }
}