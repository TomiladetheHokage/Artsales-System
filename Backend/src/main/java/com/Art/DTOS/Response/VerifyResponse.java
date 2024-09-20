package com.Art.DTOS.Response;

import lombok.*;

@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class VerifyResponse <T>{
    private T data;
}
