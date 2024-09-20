package com.Art.Services;

import com.Art.Config.PaystackConfig;
import com.Art.DTOS.Request.PaymentRequest;
import com.Art.DTOS.Response.PaymentResponse;
import com.Art.DTOS.Response.VerifyResponse;
import com.Art.Data.models.ArtWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class paymentServiceImpl implements paymentService {

    @Autowired
    private PaystackConfig paystackConfig;


    @Override
    public ResponseEntity<PaymentResponse> makePayment(PaymentRequest paymentRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = buildPayment(paymentRequest);
        try {
            ResponseEntity<PaymentResponse> response = restTemplate.postForEntity(paystackConfig.getBaseUrl(), request, PaymentResponse.class);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Error occurred while making payment: " + e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public VerifyResponse<?> verifyPayment(String reference) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+paystackConfig.getApiKey() );
        String url = paystackConfig.getVerifyUrl() + reference;

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, VerifyResponse.class).getBody();
    }

    @Override
    public String checkPayment(String reference) {
        VerifyResponse<?> response = verifyPayment(reference);
        String status = null;
        String message = response.getData().toString();
        Pattern pattern = Pattern.compile("status=(\\w+)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            status = matcher.group(1);
        }
        return status;
    }

    private HttpEntity<Map<String, Object>> buildPayment(PaymentRequest paymentRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer "+paystackConfig.getApiKey() );

        Map<String, Object> body = new HashMap<>();
        body.put("amount", paymentRequest.getAmount() * 100);
        body.put("email", paymentRequest.getEmail());

        return new HttpEntity<>(body , headers);
    }


}
