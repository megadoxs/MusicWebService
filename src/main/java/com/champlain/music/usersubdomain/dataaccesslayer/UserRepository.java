package com.champlain.music.usersubdomain.dataaccesslayer;

import com.champlain.music.usersubdomain.presentationlayer.UserResponseModel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends 
        JpaRepository<User, Integer> {
       User findUserByUserIdentifier(String userIdentifier);

//       UserResponseModel entityToResponseModel(User savedUser);
//       User findUserByUserIdentifier(UserIdentifier userIdentifier);

}
