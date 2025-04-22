package com.champlain.userservice.businesslogiclayer;

import com.champlain.userservice.dataaccesslayer.User;
import com.champlain.userservice.dataaccesslayer.UserIdentifier;
import com.champlain.userservice.dataaccesslayer.UserRepository;
import com.champlain.userservice.datamapperlayer.UserRequestMapper;
import com.champlain.userservice.datamapperlayer.UserResponseMapper;
import com.champlain.userservice.domainclientlayer.PlaylistServiceClient;
import com.champlain.userservice.presentationlayer.UserRequestModel;
import com.champlain.userservice.presentationlayer.UserResponseModel;
import com.champlain.userservice.utils.exceptions.DuplicateUsernameException;
import com.champlain.userservice.utils.exceptions.InvalidInputException;
import com.champlain.userservice.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserResponseMapper userResponseMapper;
    private final UserRequestMapper userRequestMapper;
    private final PlaylistServiceClient playlistServiceClient;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserResponseMapper userResponseMapper, UserRequestMapper userRequestMapper, PlaylistServiceClient playlistServiceClient) {
        this.userRepository = userRepository;
        this.userResponseMapper = userResponseMapper;
        this.userRequestMapper = userRequestMapper;
        this.playlistServiceClient = playlistServiceClient;
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
                throw new NotFoundException("User with userId: " + user_id + " not found.");
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

        if(getUsers().stream().anyMatch(u -> u.getUsername().equals(newUserData.getUsername())))
            throw new DuplicateUsernameException("Username " + newUserData.getUsername() + " already exists.");

        if (pw1 == null || pw2 == null || pw1.isEmpty() || pw2.isEmpty())
            throw new InvalidInputException("password can't be empty.");
        else if (pw1.equals(pw2)) {
            User user = this.userRequestMapper.requestModelToEntity(newUserData);
            user.setUserIdentifier(new UserIdentifier());
            user.setPassword(newUserData.getPassword1());
            return this.userResponseMapper.entityToResponseModel(userRepository.save(user));
        } else
            throw new InvalidInputException("passwords do not match.");
    }


    @Override
    public UserResponseModel updateUser(String userId, UserRequestModel newUserData) {
        User oldUser = userRepository.findUserByUserIdentifier_UserId(userId);
        if (oldUser != null) {
            String pw1 = newUserData.getPassword1();
            String pw2 = newUserData.getPassword2();
            if (pw1 == null || pw2 == null || pw1.isEmpty() || pw2.isEmpty())
                throw new InvalidInputException("password can't be empty.");
            else if (pw1.equals(pw2)) {
                User user = this.userRequestMapper.requestModelToEntity(newUserData);
                user.setUserIdentifier(new UserIdentifier(userId));
                user.setPassword(newUserData.getPassword1());
                user.setId(oldUser.getId());

                if(getUsers().stream().anyMatch(u -> u.getUsername().equals(newUserData.getUsername()) && !u.getUserId().equals(userId)))
                    throw new DuplicateUsernameException("Username " + newUserData.getUsername() + " already exists.");

                return this.userResponseMapper.entityToResponseModel(userRepository.save(user));
            } else
                throw new InvalidInputException("passwords do not match.");
        } else
            throw new NotFoundException("User with userId: " + userId + " not found.");
    }


    @Override
    public void deleteUserbyUserId(String userId) {
        User foundUser = this.userRepository.findUserByUserIdentifier_UserId(userId);
        if (foundUser == null)
            throw new NotFoundException("User with userId: " + userId + " not found.");
        else{
            userRepository.delete(foundUser);
            playlistServiceClient.deletePlaylistsByUserId(userId);
        }
    }
}
