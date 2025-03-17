package com.champlain.music.usersubdomain.datamapperlayer;

import com.champlain.music.usersubdomain.dataaccesslayer.User;
import com.champlain.music.usersubdomain.presentationlayer.UserController;
import com.champlain.music.usersubdomain.presentationlayer.UserResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    @Mapping(target = "userId", expression = "java(user.getUserIdentifier().getUserId())")
    UserResponseModel entityToResponseModel(User user);

    List<UserResponseModel> entityListToResponseModelList(List<User> user);

    @AfterMapping
    default void addLinks(@MappingTarget UserResponseModel userResponseModel, User user) {
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(UserController.class)
                .getUserByUserId(user.getUserIdentifier().getUserId())
        ).withSelfRel();

        userResponseModel.add(selfLink);
    }
}

