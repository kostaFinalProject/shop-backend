package com.example.shop.service;

import com.example.shop.domain.instagram.Address;
import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.DeliveryAddress;
import com.example.shop.dto.deliveryaddress.DeliveryAddressRequestDto;
import com.example.shop.dto.deliveryaddress.DeliveryAddressResponseDto;
import com.example.shop.repository.deliveryaddress.DeliveryAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final ValidationService validationService;

    @Transactional
    public void saveDeliveryAddress(Long memberId, DeliveryAddressRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);
        Address address = Address.createAddress(dto.getPostCode(), dto.getRoadAddress(), dto.getDetailAddress());

        if (deliveryAddressRepository.existByMemberAndAddress(member, address)) {
            throw new IllegalArgumentException("이미 주소록에 존재합니다.");
        }

        DeliveryAddress deliveryAddress = DeliveryAddress.createDeliveryAddress(member, address);
        deliveryAddressRepository.save(deliveryAddress);
    }

    @Transactional
    public void deleteDeliveryAddress(Long deliveryAddressId) {
        DeliveryAddress deliveryAddress = validationService.validateDeliveryAddressById(deliveryAddressId);
        deliveryAddressRepository.delete(deliveryAddress);
    }

    @Transactional(readOnly = true)
    public List<DeliveryAddressResponseDto> getDeliveryAddressList(Long memberId) {
        List<DeliveryAddress> addresses = deliveryAddressRepository.findAddressByMemberId(memberId);

        return addresses.stream()
                .map(deliveryAddress -> DeliveryAddressResponseDto.createDto(deliveryAddress.getId(),
                        deliveryAddress.getAddress().getPostCode(), deliveryAddress.getAddress().getRoadAddress(),
                        deliveryAddress.getAddress().getDetailAddress()))
                .toList();
    }
}
