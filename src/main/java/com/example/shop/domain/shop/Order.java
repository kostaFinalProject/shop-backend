package com.example.shop.domain.shop;

import com.example.shop.domain.baseentity.BaseEntity;
import com.example.shop.domain.instagram.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 주문
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Table(name = "orders")
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    private int orderPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Builder
    private Order(Member member, List<OrderItem> orderItems, int orderPrice) {
        this.member = member;
        this.orderItems = orderItems;
        this.orderPrice = orderPrice;
        this.orderStatus = OrderStatus.ORDERED;
    }

    public static Order createOrder(Member member, List<OrderItem> orderItems) {
        Order order = Order.builder().member(member).orderItems(orderItems).build();

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }

        order.setOrderPrice();

        return order;
    }

    protected void setOrderPrice() {
        this.orderPrice = getTotalOrderPrice();
    }

    /** 주문 가격 총합 */
    public int getTotalOrderPrice() {
        return orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }

    /** 주문 취소 */
    public void cancel() {
        this.orderStatus = OrderStatus.CANCELED;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
}
