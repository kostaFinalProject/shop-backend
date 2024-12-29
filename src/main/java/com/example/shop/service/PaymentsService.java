package com.example.shop.service;

import com.example.shop.domain.instagram.Member;
import com.example.shop.domain.shop.Order;
import com.example.shop.domain.shop.Payments;
import com.example.shop.dto.payment.PaymentRequestDto;
import com.example.shop.repository.PaymentsRepository;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final ValidationService validationService;
    private final PortOneApiService portOneApiService;

    /** 결제 진행 */
    @Transactional
    public void savePayment(Long memberId, PaymentRequestDto dto) {
        Member member = validationService.validateMemberById(memberId);
        Order order = validationService.validateOrderById(dto.getOrderId());

        Payment paymentInfo = portOneApiService.getPaymentInfo(dto.getImpUid());

        BigDecimal paymentAmount = paymentInfo.getAmount();
        BigDecimal orderAmount = BigDecimal.valueOf(dto.getOrderPrice());

        if (paymentAmount.compareTo(orderAmount) != 0) {
            throw new IllegalStateException("결제 금액이 일치하지 않습니다.");
        }

        if (dto.getUsePoints() == 0) {
            member.payment(order.getOrderPrice());
        } else {
            member.usePoints(dto.getUsePoints());
        }

        Payments payments = Payments.createPayment(member, order, dto.getOrderPrice(), dto.getUsePoints(), dto.getImpUid());
        paymentsRepository.save(payments);
        order.payment();
    }

    /** 결제 취소 */
    @Transactional
    public void cancelPayment(Long memberId, Long paymentId) {
        Member member = validationService.validateMemberById(memberId);
        Payments payments = validationService.validatePaymentById(paymentId);

        if (!payments.getMember().equals(member)) {
            throw new IllegalStateException("결제 취소 권한이 없습니다.");
        }

        Payment payment = portOneApiService.cancelPayment(payments.getImpUid());

        if (payments.getUsePoints() == 0) {
            payments.getMember().cancelPayment(payments.getOrder().getOrderPrice());
        } else {
            payments.getMember().cancelPoints(payments.getUsePoints());
        }
        payments.getOrder().cancel();
        paymentsRepository.delete(payments);
    }
}