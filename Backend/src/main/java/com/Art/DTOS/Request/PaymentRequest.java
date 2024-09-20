package com.Art.DTOS.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentRequest {
    private double amount;
    private String email;
    private String title;
    private String username;
}
