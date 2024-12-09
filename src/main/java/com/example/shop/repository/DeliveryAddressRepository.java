package com.example.shop.repository;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.Address;
import com.example.shop.domain.shop.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {
    boolean existsByMemberAndAddress(Member member, Address address);
    List<DeliveryAddress> findAllByMember(Member member);
}