package com.example.shop.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortOneApiService {

    private final IamportClient iamportClient;

    /**
     * 결제 정보 조회
     */
    public Payment getPaymentInfo(String impUid) {
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);

            if (response.getResponse() == null) {
                throw new IllegalStateException("결제 정보를 찾을 수 없습니다.");
            }

            return response.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("포트원 결제 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * 결제 취소
     */
    public Payment cancelPayment(String impUid) {
        try {
            CancelData cancelData = new CancelData(impUid, true);
            IamportResponse<Payment> response = iamportClient.cancelPaymentByImpUid(cancelData);

            if (response.getResponse() == null) {
                throw new IllegalStateException("결제 취소 실패");
            }

            return response.getResponse();
        } catch (Exception e) {
            throw new RuntimeException("포트원 결제 취소 중 오류 발생: " + e.getMessage(), e);
        }
    }
}
