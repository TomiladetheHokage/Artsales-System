package com.Art.DTOS.Response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordResponse {
    private String message;

    public ChangePasswordResponse(String message) {
        this.message = message;
    }
}
