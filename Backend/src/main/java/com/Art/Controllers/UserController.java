package com.Art.Controllers;

import com.Art.DTOS.Request.*;
import com.Art.DTOS.Response.*;
import com.Art.Data.Repositories.ArtworkRepo;
import com.Art.Data.Repositories.UserRepo;
import com.Art.Data.models.ArtWork;
import com.Art.Data.models.User;
import com.Art.Exceptions.*;
import com.Art.Services.UserServiceImpl;
import com.Art.Services.paymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private paymentService pay;

    @Autowired
    private ArtworkRepo artworkRepo;

    @Autowired
    private UserRepo userRepo;

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("create")
    public ResponseEntity<?> createPost(@ModelAttribute PostRequest postRequest) {
        try{
            ArtWork artWork = userService.createPost(postRequest);
            return new ResponseEntity<>(new ApiResponse(true,artWork),CREATED);
        }catch (Exception message){
            return new ResponseEntity<>(new ApiResponse(false,message.getMessage()),CREATED);
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody RegisterUserRequest registerUserRequest){
        try {
            RegisterUserResponse response = userService.signUp(registerUserRequest);
            return new ResponseEntity<>(new ApiResponse(true,response),CREATED);
        } catch (UserExistAlreadyException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()),CREATED);
        }
    }

    @GetMapping("/find/{title}")
    public ResponseEntity<?> findArtByTitle(@PathVariable String title) {
        try {
            ArtWork artWork = userService.findArtByTitle(title);
            return new ResponseEntity<>(artWork, HttpStatus.OK);
        } catch (titleNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            ChangePasswordResponse response = userService.changePassword(changePasswordRequest);
            return new ResponseEntity<>(new ApiResponse(true,response),CREATED);
        } catch (UserNameOrPasswordIsNotCorrectException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/changeUsername")
    public ResponseEntity<?> changeUsername (@RequestBody ChangeUserNameRequest changeUserNameRequest){
        try {
            ChangeUserNameResponse response = userService.changeUserName(changeUserNameRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), CREATED);
        }catch (UserNameOrPasswordIsNotCorrectException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping ("/deleteProfile")
    public ResponseEntity<?> deleteProfile(@RequestBody DeleteProfileRequest deleteProfileRequest){
        try {
            DeleteProfileResponse response = userService.deleteProfile(deleteProfileRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), CREATED);
        } catch (UserNameOrPasswordIsNotCorrectException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/find-user{username}")
    public ResponseEntity<?> findUser(@PathVariable String username){
        try {
            User user = userService.findUser(username);
            return new ResponseEntity<>(new ApiResponse(true, user),CREATED);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody LogoutRequest logoutRequest){
        try {
            LogoutResponse response = userService.logout(logoutRequest);
            return new ResponseEntity<>(new ApiResponse(true, response),CREATED);
        } catch (UserNameOrPasswordIsNotCorrectException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> purchase (@RequestBody PaymentRequest paymentRequest){
        try {
            ResponseEntity<PaymentResponse> response = userService.purchase(paymentRequest);
            return new ResponseEntity<>(new ApiResponse(true, response), CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (wrongCredentialsException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (ArtworkNotFoundEXception e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }



}
