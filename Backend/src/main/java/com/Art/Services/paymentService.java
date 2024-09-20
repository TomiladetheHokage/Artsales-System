package com.Art.Services;

import com.Art.DTOS.Request.PaymentRequest;
import com.Art.DTOS.Response.PaymentResponse;
import com.Art.DTOS.Response.VerifyResponse;
import org.springframework.http.ResponseEntity;

public interface paymentService {
    ResponseEntity<PaymentResponse> makePayment(PaymentRequest paymentRequest);
    VerifyResponse<?> verifyPayment(String reference);
    String checkPayment(String reference);
}
