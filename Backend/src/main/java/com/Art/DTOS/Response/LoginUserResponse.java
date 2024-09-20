package com.Art.DTOS.Response;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserResponse {
    private String message;

    public LoginUserResponse(String message) {
        this.message = message;
    }
}
