package com.example.shop.domain.shop;

import com.example.shop.exception.NotEnoughStockException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Domain 기능 Test (DB 사용 X)
 */
class ItemTest {

    @Test
    void createItem() {
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

        //when
        Item uniform = Item.createItem(manchesterUnited, "Adidas", "24-25 Home Replica", 150000, sizes, null);
        sSize.removeStock(100);
        mSize.addStock(200);

        //then
        assertThat(xsSize.getItem()).isEqualTo(uniform);
        assertThat(uniform.getAllStockQuantity()).isEqualTo(1300);
        assertThat(mSize.getStockQuantity()).isEqualTo(400);
        assertThatThrownBy(() -> sSize.removeStock(500)).isInstanceOf(NotEnoughStockException.class);
    }
}