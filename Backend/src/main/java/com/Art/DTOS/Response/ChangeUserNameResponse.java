package com.Art.DTOS.Response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeUserNameResponse {
    private String response;

    public ChangeUserNameResponse(String response) {
        this.response = response;
    }
}
