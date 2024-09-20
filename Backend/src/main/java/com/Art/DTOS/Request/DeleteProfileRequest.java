package com.Art.DTOS.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteProfileRequest {
    private String userName;
    private String password;
}
