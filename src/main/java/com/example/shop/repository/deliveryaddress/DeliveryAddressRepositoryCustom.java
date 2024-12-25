package com.example.shop.repository.deliveryaddress;

import com.example.shop.domain.instagram.Address;
import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.DeliveryAddress;

import java.util.List;

public interface DeliveryAddressRepositoryCustom {
    boolean existByMemberAndAddress(Member member, Address address);
    List<DeliveryAddress> findAddressByMemberId(Long memberId);
}
