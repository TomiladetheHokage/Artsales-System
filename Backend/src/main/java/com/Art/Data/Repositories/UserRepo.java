package com.Art.Data.Repositories;

import com.Art.Data.models.ArtWork;
import com.Art.Data.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepo extends MongoRepository<User, String> {
    User findByUserName(String username);
    User findUserByUserNameAndPassword(String username, String password);
    boolean existsByEmail(String email);
    List<ArtWork> availableArt (ArtWork artWork);
    User findByEmail (String email);
   // User findById (String Id);
}
