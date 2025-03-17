package com.champlain.music.usersubdomain.businesslogiclayer;


import com.champlain.music.usersubdomain.presentationlayer.UserRequestModel;
import com.champlain.music.usersubdomain.presentationlayer.UserResponseModel;

import java.util.List;

public interface UserService {

    List<UserResponseModel> getUsers();

    UserResponseModel getUserbyUserId(String user_id);

    UserResponseModel addUser(UserRequestModel newUserData);

    UserResponseModel updateUser(String userId, UserRequestModel newUserData);

    String deleteUserbyUserId(String userId);
}
