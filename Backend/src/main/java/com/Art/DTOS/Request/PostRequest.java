package com.Art.DTOS.Request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class PostRequest {
    private MultipartFile file;
    private String title;
    private String description;
    private double amount;
    private String artist;
}
