package com.example.shop.service;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.Address;
import com.example.shop.domain.shop.DeliveryAddress;
import com.example.shop.dto.DeliveryAddressDto;
import com.example.shop.dto.DeliveryAddressResponseDto;
import com.example.shop.repository.DeliveryAddressRepository;
import com.example.shop.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final ValidationService validationService;

    /** 배송지 등록 */
    public void saveDeliveryAddress(Long memberId, DeliveryAddressDto dto) {
        // Member 유효성 검사
        Member member = validationService.validateMemberById(memberId);
        Address address = Address.createAddress(dto.getPostcode(), dto.getRoadAddress(), dto.getDetailAddress());

        // 중복 확인
        boolean isDuplicate = deliveryAddressRepository.existsByMemberAndAddress(member, address);

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 동일한 배송지가 존재합니다.");
        }

        // DeliveryAddress 생성 및 저장
        DeliveryAddress deliveryAddress = DeliveryAddress.createdeliveryAddress(address, member);
        deliveryAddressRepository.save(deliveryAddress);
    }

    /** 배송지 조회 */
    public List<DeliveryAddressResponseDto> getDeliveryAddressesByMemberId(Long memberId) {
        Member member = validationService.validateMemberById(memberId);
        List<DeliveryAddress> deliveryAddresses = deliveryAddressRepository.findAllByMember(member);

        List<DeliveryAddressResponseDto> dtos = deliveryAddresses.stream()
                .map(deliveryAddress -> DeliveryAddressResponseDto.createDto(deliveryAddress.getId(), member.getId(), deliveryAddress.getAddress().getPostcode(),
                        deliveryAddress.getAddress().getRoadAddress(), deliveryAddress.getAddress().getDetailAddress()))
                .toList();

        return dtos;
    }

    /** 배송지 삭제 */
    public void deleteDeliveryAddress(Long addressId) {
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findById(addressId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송지가 존재하지 않습니다."));
        deliveryAddressRepository.delete(deliveryAddress);
    }
}