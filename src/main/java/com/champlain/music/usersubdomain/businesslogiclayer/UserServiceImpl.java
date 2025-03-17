package com.champlain.music.usersubdomain.businesslogiclayer;

import com.champlain.music.usersubdomain.dataaccesslayer.User;
import com.champlain.music.usersubdomain.dataaccesslayer.UserIdentifier;
import com.champlain.music.usersubdomain.dataaccesslayer.UserRepository;
import com.champlain.music.usersubdomain.datamapperlayer.UserRequestMapper;
import com.champlain.music.usersubdomain.datamapperlayer.UserResponseMapper;
import com.champlain.music.usersubdomain.presentationlayer.UserRequestModel;
import com.champlain.music.usersubdomain.presentationlayer.UserResponseModel;
import com.champlain.music.utils.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserRequestMapper userRequestMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserResponseMapper userResponseMapper,
                           UserRequestMapper userRequestMapper) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.userRequestMapper = userRequestMapper;
    }

    @Override
    public List<UserResponseModel> getUsers() {
        List<User> users = userRepository.findAll();
        return this.userResponseMapper.entityListToResponseModelList(users);
    }

    @Override
    public UserResponseModel getUserbyUserId(String user_id) {
        {
            User user = this.userRepository.findUserByUserIdentifier_UserId(user_id);
            UserResponseModel result;
            if (user == null) {
                throw new NotFoundException("User with " + user_id + " not found.");
            } else {
                result = this.userResponseMapper.entityToResponseModel(user);
            }
            return result;
        }
    }

    @Override
    public UserResponseModel addUser(UserRequestModel newUserData) {
        String pw1 = newUserData.getPassword1();
        String pw2 = newUserData.getPassword2();
        if (pw1 == null) {
            pw1 = "";
        }
        if (pw2 == null) {
            pw2 = "";
        }
        if (pw1.equals(pw2)) {
            User user = this.userRequestMapper.requestModelToEntity(newUserData);
            user.setUserIdentifier(new UserIdentifier());
            user.setPassword(newUserData.getPassword1());
            return this.userResponseMapper.entityToResponseModel(userRepository.save(user));
        } else
            throw new IllegalArgumentException("passwords do not match.");
    }


    @Override
    public UserResponseModel updateUser(String userId, UserRequestModel newUserData) {
        User oldUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (oldUser != null) {
            String pw1 = newUserData.getPassword1();
            String pw2 = newUserData.getPassword2();
            if (pw1 == null) {
                pw1 = "";
            }
            if (pw2 == null) {
                pw2 = "";
            }
            if (pw1.equals(pw2)) {
                User user = this.userRequestMapper.requestModelToEntity(newUserData);
                user.setUserIdentifier(new UserIdentifier(userId));
                user.setPassword(newUserData.getPassword1());
                user.setId(oldUser.getId());
                return this.userResponseMapper.entityToResponseModel(userRepository.save(user));
            } else
                throw new IllegalArgumentException("passwords do not match.");
        } else
            throw new NotFoundException("User with " + userId + " not found.");
    }


    @Override
    public void deleteUserbyUserId(String userId) {
        User foundUser = this.userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null)
            throw new NotFoundException("User with " + userId + " not found.");
        else
            this.userRepository.delete(foundUser);

    }

    // Helper methods
    public UserResponseModel fromEntityToModel(User user) {
        UserResponseModel userResponseModel =
                new UserResponseModel();
        userResponseModel.setFirstName(user.getFirstName());
        userResponseModel.setLastName(user.getLastName());

        return userResponseModel;
    }

    public List<UserResponseModel> fromEntityListToModelList(
            List<User> users) {

        List<UserResponseModel> userResponseModel =
                new ArrayList<>();
        for (User c : users) {
            userResponseModel.add(fromEntityToModel(c));
        }
        return userResponseModel;
    }

}
