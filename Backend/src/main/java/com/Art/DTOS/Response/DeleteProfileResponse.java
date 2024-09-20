package com.Art.DTOS.Response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeleteProfileResponse {
    private String response;

    public DeleteProfileResponse(String response) {
        this.response = response;
    }
}
