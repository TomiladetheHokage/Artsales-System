package com.Art.Services;

import com.Art.DTOS.Request.*;
import com.Art.DTOS.Response.*;
import com.Art.Data.models.ArtWork;
import com.Art.Data.models.User;
import com.Art.Exceptions.*;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

public interface userService {
    ArtWork createPost(PostRequest post) throws IOException, titleAlreadyExistException;
    RegisterUserResponse signUp(RegisterUserRequest registerUserRequest) throws UserExistAlreadyException;
    ArtWork findArtByTitle(String title) throws titleNotFoundException;
   // UpdateProfileResponse updateProfile(UpdateProfileRequest updateProfileRequest) throws UserNameOrPasswordIsNotCorrectException;
    ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest) throws UserNameOrPasswordIsNotCorrectException;
    ChangeUserNameResponse changeUserName(ChangeUserNameRequest changeUserNameRequest) throws UserNameOrPasswordIsNotCorrectException;
    DeleteProfileResponse deleteProfile(DeleteProfileRequest deleteProfileRequest) throws UserNameOrPasswordIsNotCorrectException;
    User findUser(String username) throws UserNotFoundException;
    LoginUserResponse login(LoginUserRequest loginUserRequest) throws UserNameOrPasswordIsNotCorrectException;
    LogoutResponse logout(LogoutRequest logoutRequest) throws UserNameOrPasswordIsNotCorrectException;
    List<ArtWork> displayAllArt(String userName, String email);
    ResponseEntity<PaymentResponse> purchase (PaymentRequest paymentRequest) throws UserNameOrPasswordIsNotCorrectException, UserNotFoundException, wrongCredentialsException, ArtworkNotFoundEXception;
    void deleteAllArt (String userName, String email);
    void deleteArt (String userName, String email);
}
