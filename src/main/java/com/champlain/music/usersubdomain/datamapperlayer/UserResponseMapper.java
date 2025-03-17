package com.champlain.music.usersubdomain.datamapperlayer;

import com.champlain.music.usersubdomain.dataaccesslayer.User;
import com.champlain.music.usersubdomain.presentationlayer.UserResponseModel;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    @Mapping(target = "userId", expression = "java(user.getUserIdentifier())")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "email", source = "email")


    UserResponseModel entityToResponseModel(User user);
    List<UserResponseModel> entityListToResponseModelList(List<User> user);
}

