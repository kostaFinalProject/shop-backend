package com.example.shop.controller;

import com.example.shop.domain.shop.Address;
import com.example.shop.dto.DeliveryAddressDto;
import com.example.shop.service.DeliveryAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveryaddress")
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    /** 배송지 조회 */
    @GetMapping("/{memberId}")
    public ResponseEntity<?> getDeliveryAddresses(@PathVariable Long memberId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(deliveryAddressService.getDeliveryAddressesByMemberId(memberId));
    }

    /** 배송지 등록 */
    @PostMapping("/{memberId}")
    public ResponseEntity<String> saveDeliveryAddress(@PathVariable Long memberId, @RequestBody DeliveryAddressDto dto) {
        try {
            deliveryAddressService.saveDeliveryAddress(memberId, dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("배송지가 저장되었습니다.");
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    /** 배송지 삭제 */
    @DeleteMapping("/{addressId}")
    public ResponseEntity<String> deleteDeliveryAddress(@PathVariable("addressId") Long addressId) {
        deliveryAddressService.deleteDeliveryAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).body("배송지가 삭제되었습니다.");
    }
}
