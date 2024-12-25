package com.example.shop.repository.deliveryaddress;

import com.example.shop.domain.shop.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long>, DeliveryAddressRepositoryCustom {
}
