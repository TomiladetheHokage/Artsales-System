package com.Art.Services;

import com.Art.DTOS.Request.*;
import com.Art.DTOS.Response.*;
import com.Art.Data.Repositories.ArtworkRepo;
import com.Art.Data.Repositories.UserRepo;
import com.Art.Data.models.ArtWork;
import com.Art.Data.models.User;
import com.Art.Exceptions.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class UserServiceImplTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ArtworkRepo artworkRepo;

    @Autowired
    private UserServiceImpl userService;

    @BeforeEach
   public void setUp() {
     userRepo.deleteAll();
      artworkRepo.deleteAll();
    }

    @Test
    public void testUserCanCreatePost(){
        PostRequest post = new PostRequest();
        Path path = Paths.get("C:\\Users\\OWNER\\Downloads\\ArtSalesSystem\\src\\main\\resources\\static\\Screenshot 2024-08-05 120706.png");

        try(InputStream inputStream = Files.newInputStream(path)){
            MultipartFile file = new MockMultipartFile("file", inputStream);
            post.setTitle("cold");
            post.setDescription("kai");
            post.setFile(file);
            post.setAmount(5000);


            ArtWork artWork = userService.createPost(post);
            assertThat(artWork.getImageUrl()).isNotNull();

        } catch (IOException | titleAlreadyExistException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testUserCanSignUp() throws UserExistAlreadyException {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setEmail("tomi@gmail.com");
        registerUserRequest.setPassword("123456");
        registerUserRequest.setUserName("tomi");

        RegisterUserResponse response = userService.signUp(registerUserRequest);
        assertNotNull(response);
        assertEquals("tomi", registerUserRequest.getUserName());
        assertEquals("tomi@gmail.com", registerUserRequest.getEmail());
        assertEquals("123456", registerUserRequest.getPassword());
    }

    @Test
    public void testUserCannotRegisterWithTheSameEmail() throws UserExistAlreadyException {
        RegisterUserRequest firstUser = new RegisterUserRequest();
        firstUser.setUserName("tomi");
        firstUser.setPassword("123456");
        firstUser.setEmail("tomi@gmail.com");
        userService.signUp(firstUser);

        RegisterUserRequest secondUser = new RegisterUserRequest();
        secondUser.setUserName("romi");
        secondUser.setPassword("55555");
        secondUser.setEmail("tomi@gmail.com");

        UserExistAlreadyException exception = assertThrows(UserExistAlreadyException.class, () -> {
            userService.signUp(secondUser);
        });
        Assertions.assertEquals("Email already exist", exception.getMessage());
    }


    @Test
    public void testUserCanFindArtworkByTitle() throws IOException, titleAlreadyExistException, titleNotFoundException {
        PostRequest post = new PostRequest();
        Path path = Paths.get("C:\\Users\\OWNER\\Downloads\\ArtSalesSystem\\src\\main\\resources\\static\\Screenshot 2024-08-05 120706.png");
        InputStream inputStream = Files.newInputStream(path);
        MultipartFile file = new MockMultipartFile("file", inputStream);

        String title = "omo";

        post.setTitle(title);
        post.setDescription("some-description");
        post.setFile(file);
        post.setAmount(500);

        userService.createPost(post);
        ArtWork foundArt =  userService.findArtByTitle(title);
        Assertions.assertEquals(title, foundArt.getTitle());
    }


    @Test
    public void testUserCanChangePassword() throws UserExistAlreadyException, UserNameOrPasswordIsNotCorrectException {
        RegisterUserRequest user = new RegisterUserRequest();
        user.setUserName("tomi");
        user.setPassword("123");
        user.setEmail("tomi@gmail.com");
        userService.signUp(user);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("123");
        changePasswordRequest.setNewPassword("123456");
        changePasswordRequest.setUsername("tomi");

        ChangePasswordResponse response = userService.changePassword(changePasswordRequest);

        assertNotNull(response);
        assertEquals("successfully changed password", response.getMessage());
    }

    @Test
    public void testUserCanChangeUsername() throws UserExistAlreadyException, UserNameOrPasswordIsNotCorrectException {
        RegisterUserRequest user = new RegisterUserRequest();
        user.setUserName("tomi");
        user.setPassword("123");
        user.setEmail("tomi@gmail.com");
        userService.signUp(user);

        ChangeUserNameRequest changeUserNameRequest = new ChangeUserNameRequest();
        changeUserNameRequest.setOldUserName("tomi");
        changeUserNameRequest.setPassword("123");
        changeUserNameRequest.setNewUserName("HEIS");

        ChangeUserNameResponse response = userService.changeUserName(changeUserNameRequest);

        assertNotNull(response);
        assertEquals("Successfully changed Username", response.getResponse());
    }

    @Test
    public void testUserCanDeleteProfile() throws UserNameOrPasswordIsNotCorrectException, UserExistAlreadyException {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUserName("tomi");
        registerUserRequest.setPassword("123456");
        userService.signUp(registerUserRequest);

        DeleteProfileRequest deleteProfileRequest = new DeleteProfileRequest();
        deleteProfileRequest.setUserName("tomi");
        deleteProfileRequest.setPassword("123456");
        DeleteProfileResponse response =  userService.deleteProfile(deleteProfileRequest);

        assertEquals("Successfully deleted", response.getResponse());
    }

    @Test
    public void testUserCanFindUser() throws UserNotFoundException, UserExistAlreadyException {
       RegisterUserRequest user = new RegisterUserRequest();
       user.setUserName("tomi");
       user.setPassword("123456");
       user.setEmail("tomi@gmail.com");

       userService.signUp(user);

        User foundUser = userService.findUser("tomi");
        assertEquals("tomi", foundUser.getUserName());
    }

    @Test
    public void testUserCanLogin() throws UserNameOrPasswordIsNotCorrectException, UserExistAlreadyException {
       RegisterUserRequest user = new RegisterUserRequest();
       user.setUserName("tomilade");
       user.setPassword("123456");
       user.setEmail("tomi@gmail.com");

       userService.signUp(user);

       LoginUserRequest loginUserRequest = new LoginUserRequest();
       loginUserRequest.setUsername("tomilade");
       loginUserRequest.setPassword("123456");

       LoginUserResponse response =  userService.login(loginUserRequest);

       assertEquals("Successfully logged in", response.getMessage());
    }

    @Test
    public void testUserCanLogout() throws UserNameOrPasswordIsNotCorrectException, UserExistAlreadyException {
        RegisterUserRequest user = new RegisterUserRequest();
        user.setUserName("romi");
        user.setPassword("123456");
        user.setEmail("tomi@gmail.com");
        userService.signUp(user);

        LogoutRequest logout = new LogoutRequest();
        logout.setUsername("romi");
        logout.setPassword("123456");
        LogoutResponse response =  userService.logout(logout);

        assertEquals("Successfully logged out", response.getMessage());
    }

    @Test
    public void testUserCanPurchase() throws UserExistAlreadyException, UserNotFoundException, IOException, titleAlreadyExistException, wrongCredentialsException, ArtworkNotFoundEXception, titleNotFoundException {
        RegisterUserRequest user = new RegisterUserRequest();
        user.setUserName("tomi");
        user.setPassword("123456");
        user.setEmail("tomi@gmail.co");
        userService.signUp(user);

        Path path = Paths.get("C:\\Users\\OWNER\\Downloads\\ArtSalesSystem\\src\\main\\resources\\static\\Screenshot 2024-08-05 120706.png");
        InputStream inputStream = Files.newInputStream(path);
        MultipartFile file = new MockMultipartFile("file", inputStream);

        PostRequest postRequest = new PostRequest();
        postRequest.setDescription("A beautiful painting");
        postRequest.setTitle("omo");
        postRequest.setAmount(20);
        postRequest.setFile(file);
        userService.createPost(postRequest);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setEmail("tomi@gmail.co");
        paymentRequest.setAmount(20);
        paymentRequest.setTitle("omo");

        ResponseEntity<PaymentResponse> response = userService.purchase(paymentRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUserCanNotPurchaseTwice() throws UserExistAlreadyException, UserNotFoundException, IOException, titleAlreadyExistException, wrongCredentialsException, ArtworkNotFoundEXception, titleNotFoundException {
        RegisterUserRequest user = new RegisterUserRequest();
        user.setUserName("tomi");
        user.setPassword("123456");
        user.setEmail("tomi@gmail.com");
        userService.signUp(user);

        Path path = Paths.get("C:\\Users\\OWNER\\Downloads\\ArtSalesSystem\\src\\main\\resources\\static\\Screenshot 2024-08-05 120706.png");
        InputStream inputStream = Files.newInputStream(path);
        MultipartFile file = new MockMultipartFile("file", inputStream);

        PostRequest postRequest = new PostRequest();
        postRequest.setDescription("A beautiful painting");
        postRequest.setTitle("omo");
        postRequest.setAmount(20);
        postRequest.setFile(file);
        userService.createPost(postRequest);

        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setEmail("tomi@gmail.com");
        paymentRequest.setAmount(20);
        paymentRequest.setTitle("omo");

        ResponseEntity<PaymentResponse> response = userService.purchase(paymentRequest);


        paymentRequest.setEmail("tomi@gmail.comm");
        paymentRequest.setAmount(20);
        paymentRequest.setTitle("omo");

        ResponseEntity<PaymentResponse> secondResponse = userService.purchase(paymentRequest);

        assertNotNull(secondResponse);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testPurchaseWithNonExistentArtwork() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setEmail("tomi@gmail.co");
        paymentRequest.setAmount(20);
        paymentRequest.setTitle("non-existent-title");

        ArtworkNotFoundEXception exception = assertThrows(ArtworkNotFoundEXception.class, () -> {
            userService.purchase(paymentRequest);
        });

        assertEquals("Art work does not exist", exception.getMessage());
    }

    @Test
    public void testUserCanNotFindArtworkByNonExistentTitle() {
        titleNotFoundException exception = assertThrows(titleNotFoundException.class, () -> {
            userService.findArtByTitle("non-existent-title");
        });
        assertEquals("Artwork with title non-existent-title not found", exception.getMessage());
    }


    @Test
    public void testUserCannotDeleteProfileWithWrongPassword() throws Exception {
        RegisterUserRequest user1 = new RegisterUserRequest();
        user1.setUserName("tomi");
        user1.setPassword("123456");
        user1.setEmail("tomi@gmail.com");
        userService.signUp(user1);

        DeleteProfileRequest deleteProfileRequest = new DeleteProfileRequest();
        deleteProfileRequest.setUserName("tomi");
        deleteProfileRequest.setPassword("abcdef");

        UserNameOrPasswordIsNotCorrectException exception = assertThrows(UserNameOrPasswordIsNotCorrectException.class, () -> {
            userService.deleteProfile(deleteProfileRequest);
        });

        assertEquals("Username or password is incorrect", exception.getMessage());
    }

    @Test
    public void testUserCannotChangePasswordWithWrongCredentials() throws UserExistAlreadyException, UserNameOrPasswordIsNotCorrectException {
        RegisterUserRequest user = new RegisterUserRequest();
        user.setUserName("tomi");
        user.setPassword("123456");
        user.setEmail("tomi@gmail.com");
        userService.signUp(user);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setOldPassword("wrong-password");
        changePasswordRequest.setNewPassword("new-password");
        changePasswordRequest.setUsername("tomi");

        UserNameOrPasswordIsNotCorrectException exception = assertThrows(UserNameOrPasswordIsNotCorrectException.class, () -> {
            userService.changePassword(changePasswordRequest);
        });

        assertEquals("Wrong username or password", exception.getMessage());
    }

    @Test
    public void testFindArtByTitleThrowsExceptionWhenTitleNotFound() {
        String nonExistentTitle = "NonExistentTitle";
        assertThrows(titleNotFoundException.class, () -> {
            userService.findArtByTitle(nonExistentTitle);
        });
    }




}