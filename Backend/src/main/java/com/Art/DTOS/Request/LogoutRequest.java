package com.Art.DTOS.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogoutRequest {
    private String username;
    private String password;

}
