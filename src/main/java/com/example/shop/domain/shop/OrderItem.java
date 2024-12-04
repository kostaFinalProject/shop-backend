package com.example.shop.domain.shop;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 주문 상품
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_size_id")
    private ItemSize itemSize;

    private int itemPrice;
    private int count;

    @Builder
    private OrderItem(ItemSize itemSize, int itemPrice, int count) {
        this.itemSize = itemSize;
        this.itemPrice = itemPrice;
        this.count = count;
    }

    public static OrderItem createOrderItem(ItemSize itemSize, int itemPrice, int count) {
        itemSize.removeStock(count);
        return OrderItem.builder().itemSize(itemSize).itemPrice(itemPrice).count(count).build();
    }

    /** 연관 관계 메서드 */
    protected void setOrder(Order order) {
        this.order = order;
        if (!order.getOrderItems().contains(this)) {
            order.getOrderItems().add(this);
        }
    }

    /** 주문 상품 당 가격*/
    public int getTotalPrice() {
        return getItemPrice() * getCount();
    }

    /** 주문 취소 */
    public void cancel() {
        getItemSize().addStock(count);
    }
}
