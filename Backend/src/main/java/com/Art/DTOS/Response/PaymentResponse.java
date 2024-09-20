package com.Art.DTOS.Response;

import com.Art.Utils.DataObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@NoArgsConstructor
public class PaymentResponse {
    private boolean status;
    private String message;
    @JsonProperty("data")
    private DataObject data;
}
