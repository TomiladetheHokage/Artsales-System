package com.Art.Utils;

import com.Art.DTOS.Request.RegisterUserRequest;
import com.Art.DTOS.Request.UpdateProfileRequest;
import com.Art.Data.models.User;

public class Mapper {
    public static User map (RegisterUserRequest registerUserRequest){
        User user = new User();
        user.setUserName(registerUserRequest.getUserName());
        user.setPassword(registerUserRequest.getPassword());
        user.setEmail(registerUserRequest.getEmail());
        return user;
    }

    public static User map (UpdateProfileRequest updateProfileRequest){
        User user = new User();
        user.setUserName(updateProfileRequest.getCurrentUserName());
        user.setPassword(updateProfileRequest.getCurrentPassword());
        user.setEmail(updateProfileRequest.getCurrentEmail());
        return user;
    }

   // public static User map
}
