package com.example.shop.domain.shop;

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

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "delivery_address_member")
    private Member member;

    @Builder
    private DeliveryAddress(Address address, Member member){
        this.address = address;
        this.member = member;
    }

    public static DeliveryAddress createdeliveryAddress(Address address, Member member){
        return DeliveryAddress.builder().address(address).member(member).build();
    }
}
