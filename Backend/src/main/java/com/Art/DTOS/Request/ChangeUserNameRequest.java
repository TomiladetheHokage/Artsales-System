package com.Art.DTOS.Request;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ChangeUserNameRequest {
    private String oldUserName;
    private String newUserName;
    private String password;

}




