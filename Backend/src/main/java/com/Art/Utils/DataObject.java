package com.Art.Utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class DataObject {
    private String authorization_url;
    private String access_code;
    private String reference;
}
