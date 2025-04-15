package com.champlain.userservice.businesslogiclayer;


import com.champlain.userservice.presentationlayer.UserRequestModel;
import com.champlain.userservice.presentationlayer.UserResponseModel;

import java.util.List;

public interface UserService {

    List<UserResponseModel> getUsers();

    UserResponseModel getUserbyUserId(String user_id);

    UserResponseModel addUser(UserRequestModel newUserData);

    UserResponseModel updateUser(String userId, UserRequestModel newUserData);

    void deleteUserbyUserId(String userId);
}
