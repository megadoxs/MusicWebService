package com.champlain.apigatewayservice.buisnesslogiclayer;

import com.champlain.apigatewayservice.domainclientlayer.UserServiceClient;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserRequestModel;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserResponseModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserServiceClient userServiceClient;

    public UserServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public List<UserResponseModel> getUsers() {
        return userServiceClient.getUsers();
    }

    @Override
    public UserResponseModel getUserById(String userId) {
        return userServiceClient.getUserById(userId);
    }

    @Override
    public UserResponseModel addUser(UserRequestModel userRequestModel) {
        return userServiceClient.addUser(userRequestModel);
    }

    @Override
    public UserResponseModel updateUser(UserRequestModel userRequestModel, String userId) {
        return userServiceClient.updateUser(userRequestModel, userId);
    }

    @Override
    public void deleteUser(String userId) {
        userServiceClient.deleteUser(userId);
    }
}
