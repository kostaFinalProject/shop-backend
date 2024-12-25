package com.example.shop.repository.deliveryaddress;

import com.example.shop.domain.instagram.Address;
import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.DeliveryAddress;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.shop.domain.instagram.QMember.member;
import static com.example.shop.domain.shop.QDeliveryAddress.deliveryAddress;

@RequiredArgsConstructor
public class DeliveryAddressRepositoryImpl implements DeliveryAddressRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existByMemberAndAddress(Member member, Address address) {
        return queryFactory.selectOne()
                .from(deliveryAddress)
                .where(deliveryAddress.member.id.eq(member.getId())
                        .and(deliveryAddress.address.postCode.eq(address.getPostCode()))
                        .and(deliveryAddress.address.roadAddress.eq(address.getRoadAddress()))
                        .and(deliveryAddress.address.detailAddress.eq(address.getDetailAddress())))
                .fetchFirst() != null;
    }

    @Override
    public List<DeliveryAddress> findAddressByMemberId(Long memberId) {
        return queryFactory.selectFrom(deliveryAddress)
                .join(deliveryAddress.member, member)
                .where(deliveryAddress.member.id.eq(memberId))
                .fetch();
    }
}
