package com.Art.DTOS.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateProfileRequest {
        private String currentUserName;
        private String currentPassword;
        private String currentEmail;

        private String newPassword;
        private String newEmail;
        private String newUsername;
    }
