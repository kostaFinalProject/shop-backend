package com.example.shop.controller;

import com.example.shop.aop.SecurityAspect;
import com.example.shop.dto.payment.PaymentRequestDto;
import com.example.shop.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentsController {

    private final PaymentsService paymentsService;

    /** 결제 요청 */
    @PostMapping
    public ResponseEntity<?> savePayments(@RequestBody PaymentRequestDto dto) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        paymentsService.savePayment(memberId, dto);
        return ResponseEntity.status(HttpStatus.OK).body("결제가 완료되었습니다.");
    }

    /** 결제 취소 */
    @DeleteMapping("/{paymentsId}")
    public ResponseEntity<?> deletePayments(@PathVariable("paymentsId") Long paymentsId) {
        Long memberId = SecurityAspect.getCurrentMemberId();
        paymentsService.cancelPayment(memberId, paymentsId);
        return ResponseEntity.status(HttpStatus.OK).body("결제가 취소되었습니다.");
    }
}