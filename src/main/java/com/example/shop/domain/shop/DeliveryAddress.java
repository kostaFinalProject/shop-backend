package com.example.shop.domain.shop;

import com.example.shop.domain.instagram.Address;
import com.example.shop.domain.instagram.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeliveryAddress {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Address address;

    @Builder
    private DeliveryAddress(Member member, Address address) {
        this.member = member;
        this.address = address;
    }

    public static DeliveryAddress createDeliveryAddress(Member member, Address address) {
        return DeliveryAddress.builder().member(member).address(address).build();
    }
}
