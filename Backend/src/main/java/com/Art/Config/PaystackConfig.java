package com.Art.Config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration
public class PaystackConfig {
    @Value("${paystack.api.key}")
    private String apiKey;

    @Value("${pay.stack.baseUrl}")
    private String baseUrl;

    @Value("${paystack.verify_url}")
    private String verifyUrl;
}
