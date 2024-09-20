package com.Art.Services;

import com.Art.DTOS.Request.*;
import com.Art.DTOS.Response.*;
import com.Art.Data.Repositories.ArtworkRepo;
import com.Art.Data.Repositories.UserRepo;
import com.Art.Data.models.ArtWork;
import com.Art.Data.models.User;
import com.Art.Data.models.userRoles;
import com.Art.Exceptions.*;
import com.Art.Utils.DataObject;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.Art.Utils.Mapper.map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements userService{

    private final ArtworkRepo artworkRepo;
    private final Cloudinary cloudinary;
    private final UserRepo userRepo;

    @Autowired
    paymentServiceImpl pay = new paymentServiceImpl();


    DataObject data = new DataObject();

    @Override
    public ArtWork createPost(PostRequest post) throws IOException, titleAlreadyExistException {
        validate(post.getTitle());
        ArtWork artwork = buildArtwork(post);
        String url =  uploadImage(post.getFile());
        artwork.setImageUrl(url);
        userRepo.availableArt(artwork);
        return artworkRepo.save(artwork);
    }

    @Override
    public RegisterUserResponse signUp(RegisterUserRequest registerUserRequest) throws UserExistAlreadyException {
        validateEmail(registerUserRequest.getEmail());
        User user = map(registerUserRequest);
        user.setRole(userRoles.USER);
        user.setLoginStatus(true);
        userRepo.save(user);
        RegisterUserResponse registerUserResponse = new RegisterUserResponse();
        registerUserResponse.setMessage("Successfully registered");
        return registerUserResponse;
    }

    @Override
    public ArtWork findArtByTitle(String title) throws titleNotFoundException {
        ArtWork artWork = artworkRepo.findByTitle(title);
        if (artWork != null) return artWork;
        else throw new titleNotFoundException("Artwork with title " + title + " not found");
    }


    @Override
    public ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest) throws UserNameOrPasswordIsNotCorrectException {
        User existingUser = userRepo.findUserByUserNameAndPassword(changePasswordRequest.getUsername(), changePasswordRequest.getOldPassword());
        if (existingUser == null) throw new UserNameOrPasswordIsNotCorrectException("Wrong username or password");
        existingUser.setPassword(changePasswordRequest.getNewPassword());
        userRepo.save(existingUser);
        return new ChangePasswordResponse("successfully changed password");
    }


    @Override
    public ChangeUserNameResponse changeUserName(ChangeUserNameRequest changeUserNameRequest) throws UserNameOrPasswordIsNotCorrectException {
        User existingUser = userRepo.findUserByUserNameAndPassword(changeUserNameRequest.getOldUserName(), changeUserNameRequest.getPassword() );
        if (existingUser == null) throw new UserNameOrPasswordIsNotCorrectException("Wrong username or password");
        existingUser.setUserName(changeUserNameRequest.getNewUserName());
        userRepo.save(existingUser);
        return new ChangeUserNameResponse("Successfully changed Username");
    }


    @Override
    public DeleteProfileResponse deleteProfile(DeleteProfileRequest deleteProfileRequest) throws UserNameOrPasswordIsNotCorrectException {
       User existingUser = userRepo.findUserByUserNameAndPassword( deleteProfileRequest.getUserName(), deleteProfileRequest.getPassword());
       if (existingUser == null) throw new UserNameOrPasswordIsNotCorrectException("Username or password is incorrect");
       userRepo.delete(existingUser);
      return new DeleteProfileResponse("Successfully deleted");
    }


    @Override
    public User findUser(String username) throws UserNotFoundException {
        User user = userRepo.findByUserName(username);
        if (user != null) return user;
        else throw new UserNotFoundException(String.format("%s not found ", username));
    }

    @Override
    public LoginUserResponse login(LoginUserRequest loginUserRequest) throws UserNameOrPasswordIsNotCorrectException {
        User user = userRepo.findUserByUserNameAndPassword(loginUserRequest.getUsername(), loginUserRequest.getPassword());
        if(user != null){
            user.setRole(userRoles.USER);
            return new LoginUserResponse("Successfully logged in");
        }
        else throw new UserNameOrPasswordIsNotCorrectException(String.format("%s or %s is not correct", loginUserRequest.getUsername(), loginUserRequest.getPassword()));
    }

    @Override
    public LogoutResponse logout(LogoutRequest logoutRequest) throws UserNameOrPasswordIsNotCorrectException {
        User existingUser = userRepo.findUserByUserNameAndPassword(logoutRequest.getUsername(), logoutRequest.getPassword());
        if(existingUser != null){
            existingUser.setLoginStatus(false);
            userRepo.save(existingUser);
            return new LogoutResponse("Successfully logged out");
        }
        else throw new UserNameOrPasswordIsNotCorrectException(String.format("%s or %s is  incorrect ", logoutRequest.getUsername(), logoutRequest.getPassword()));
    }

    @Override
    public List<ArtWork> displayAllArt(String userName, String email) {
        return artworkRepo.findAll();
    }

    @Override
    public ResponseEntity<PaymentResponse> purchase(PaymentRequest paymentRequest) throws UserNotFoundException, wrongCredentialsException, ArtworkNotFoundEXception {
        ArtWork artWork = artworkRepo.findByTitle(paymentRequest.getTitle());
        if(artWork == null) throw new ArtworkNotFoundEXception("Art work does not exist");

        if(!artWork.isAvalable()) throw new ArtworkNotFoundEXception("Art work is not available");

        User existingUser = userRepo.findByEmail( paymentRequest.getEmail());
        if(existingUser == null) throw new UserNotFoundException(String.format("%s not found", paymentRequest.getEmail()));

        if(paymentRequest.getAmount() == artWork.getAmount()) {
            ResponseEntity<PaymentResponse> response = pay.makePayment(paymentRequest);
           // String reference = data.getReference();
          //  if (pay.checkPayment(reference).equals("successful")) {
                if (artWork.isAvalable()) artWork.setAvalable(false);
                artworkRepo.save(artWork);
           // }
                return response;
            }

        else throw new wrongCredentialsException("Payment amount does not match artwork price.");
    }


    @Override
    public void deleteAllArt(String userName, String email) {

    }

    @Override
    public void deleteArt(String userName, String email) {

    }


    private boolean titleExists(String title) {
       List<ArtWork> art =  artworkRepo.findAll();
       for (ArtWork artWork : art) {
           if (artWork.getTitle().equals(title)) {
               return true;
           }
       }
       return false;
    }
    private ArtWork buildArtwork (PostRequest post) throws IOException {
        ArtWork artWork = new ArtWork();
        artWork.setTitle(post.getTitle());
        artWork.setDescription(post.getDescription());
        artWork.setAmount(post.getAmount());
        artWork.setAvalable(true);
        artWork.setAvailability("true");
        return artWork;
    }
    private void validate(String title) throws titleAlreadyExistException {
        if(titleExists(title)) throw new titleAlreadyExistException(String.format("%s  already exist", title));
    }
    private String uploadImage(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader().upload(multipartFile.getBytes(), Map.of("public_id", UUID.randomUUID().toString())).get("url").toString();
    }
    private void validateEmail(String email) throws UserExistAlreadyException {
        if(userRepo.existsByEmail(email)) throw new UserExistAlreadyException("Email already exist");
    }

}
