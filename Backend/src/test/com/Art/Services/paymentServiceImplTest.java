package com.Art.Services;

import com.Art.DTOS.Request.PaymentRequest;
import com.Art.DTOS.Response.PaymentResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class paymentServiceImplTest {

    @Autowired
    private paymentServiceImpl paymentService;

    @Test
    public void paymentServiceTest() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(1000);
        paymentRequest.setEmail("tomi@gmail.com");

        ResponseEntity<PaymentResponse> response = paymentService.makePayment(paymentRequest);

        //assertTrue(paymentRequest.getAmount() > 0);
        assertTrue(response.getBody().isStatus());
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testVerifyPayment() {
        String status = paymentService.checkPayment("98vrhum6q1");
        System.out.println(status);
    }

}