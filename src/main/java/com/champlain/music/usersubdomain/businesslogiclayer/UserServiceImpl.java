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
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
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
            UserIdentifier uid = new UserIdentifier(user_id);
            User user = this.userRepository.findUserByUserIdentifier(uid.getUserId());
            UserResponseModel result = null;
            if (user == null) {
                throw new NotFoundException("User with " + user_id  + " not found.");
            } else {
                result = this.userResponseMapper.entityToResponseModel(user);
            }
            return result;
        }
    }

    @Override
    public UserResponseModel addUser(UserRequestModel newUserData) {
        return null;
    }

//    @Override
//    public UserResponseModel addUser(UserRequestModel newUserData) {
//        String message = "";
//        User savedUser = new User();
//        String pw1 = newUserData.getPassword1();
//        String pw2 = newUserData.getPassword2();
//        if (pw1 == null) {
//            pw1 = "";
//        }
//        if (pw2 == null) {
//            pw2 = "";
//        }
//        if (!pw1.equals(pw2)) {
//            message = "Entered passwords do not match!";
//        } else {
//            UserIdentifier userIdentifier = new UserIdentifier();
//            User user = this.userRequestMapper.requestModelToEntity(
//                    newUserData, userIdentifier
//            );
//            user.setPassword(newUserData.getPassword1());
//            savedUser = this.userRepository.save(user);
//            if (savedUser != null)
//                message = "User saved successfully.";
//            else
//                message = "Could not save new User into repository.";
//
//        }
//        return this.userResponseMapper.entityToResponseModel(savedUser);
//    }

    @Override
    public UserResponseModel updateUser(String userId, UserRequestModel newUserData) {
        String message = "";
        User savedUser = new User();
        User foundUser = this.userRepository.findUserByUserIdentifier(userId);

        if (foundUser == null) {
            message = "User with id: " + userId + " not found in repository.";
        } else {
            String pw1 = newUserData.getPassword1();
            String pw2 = newUserData.getPassword2();
            if (pw1 == null) {
                pw1 = "";
            }
            if (pw2 == null) {
                pw2 = "";
            }

            if (pw1.equals(pw2)) {
                User customer = this.userRequestMapper.requestModelToEntity(newUserData, newUserData.getUserIdentifier());
                customer.setPassword(newUserData.getPassword1());
                customer.setUserIdentifier(userId); // important
                customer.setId(foundUser.getId());  // important
                savedUser = this.userRepository.save(customer);
                message = (savedUser != null) ? "User updated successfully." : "Could not save new user into repository.";
            } else {
                message = "Entered passwords do not match!";
            }
        }
        // Transform entity to response model using a mapper in the service layer
        return this.userResponseMapper.entityToResponseModel(savedUser);
    }


    @Override
    public String deleteUserbyUserId(String userId) {
        String message = "";
        User foundUser = this.userRepository.findUserByUserIdentifier(userId);
        if (foundUser==null){
            message = "User with id: " + userId + " not found in repository.";
        } else {

            this.userRepository.delete(foundUser);
            message =  "User with id: " + userId + " deleted successfully.";
        }
        return message;
    }

    // Helper methods
    public UserResponseModel fromEntityToModel(User user){
        UserResponseModel userResponseModel =
                new UserResponseModel();
        userResponseModel.setFirstName(user.getFirstName());
        userResponseModel.setLastName(user.getLastName());

        return userResponseModel;
    }
    public List<UserResponseModel> fromEntityListToModelList(
            List<User> users){

        List<UserResponseModel> userResponseModel =
                new ArrayList<>();
        for (User c : users) {
            userResponseModel.add(fromEntityToModel(c));
        }
        return userResponseModel;
    }

}
