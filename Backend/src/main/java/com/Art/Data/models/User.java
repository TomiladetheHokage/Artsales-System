package com.Art.Data.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String userName;
    private String password;
    private String email;
    private userRoles role;
    private boolean loginStatus;
    private List<ArtWork> availableArt = new ArrayList<>();
}
