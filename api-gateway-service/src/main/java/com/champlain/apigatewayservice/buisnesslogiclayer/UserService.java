package com.champlain.apigatewayservice.buisnesslogiclayer;

import com.champlain.apigatewayservice.presentationlayer.userdto.UserRequestModel;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserResponseModel;

import java.util.List;

public interface UserService {
    List<UserResponseModel> getUsers();

    UserResponseModel getUserById(String userId);

    UserResponseModel addUser(UserRequestModel userRequestModel);

    UserResponseModel updateUser(UserRequestModel userRequestModel, String userId);

    void deleteUser(String userId);
}
