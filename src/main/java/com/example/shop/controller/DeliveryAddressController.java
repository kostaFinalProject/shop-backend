package com.example.shop.controller;

import com.example.shop.aop.SecurityAspect;
import com.example.shop.dto.deliveryaddress.DeliveryAddressRequestDto;
import com.example.shop.service.DeliveryAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/delivery-address")
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    @PostMapping
    public ResponseEntity<?> saveDeliveryAddress(@RequestBody DeliveryAddressRequestDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        deliveryAddressService.saveDeliveryAddress(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("주소록에 등록되었습니다.");
    }

    @GetMapping
    public ResponseEntity<?> getDeliveryAddressList() {
        Long memberId = SecurityAspect.getCurrentMemberId();
        return ResponseEntity.status(HttpStatus.OK).body(deliveryAddressService.getDeliveryAddressList(memberId));
    }

    @DeleteMapping("/{deliveryAddressId}")
    public ResponseEntity<?> deleteDeliveryAddress(@PathVariable("deliveryAddressId") Long deliveryAddressId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        deliveryAddressService.deleteDeliveryAddress(deliveryAddressId);
        return ResponseEntity.status(HttpStatus.OK).body("주소록에서 삭제되었습니다.");
    }
}
