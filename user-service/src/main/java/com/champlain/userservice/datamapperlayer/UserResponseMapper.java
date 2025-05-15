package com.champlain.userservice.datamapperlayer;

import com.champlain.userservice.dataaccesslayer.User;
import com.champlain.userservice.presentationlayer.UserController;
import com.champlain.userservice.presentationlayer.UserResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    @Mapping(target = "userId", expression = "java(user.getUserIdentifier().getUserId())")
    UserResponseModel entityToResponseModel(User user);

    List<UserResponseModel> entityListToResponseModelList(List<User> user);

    @AfterMapping
    default void addLinks(@MappingTarget UserResponseModel userResponseModel, User user) {
        UriComponentsBuilder baseUri = UriComponentsBuilder.fromHttpUrl("http://localhost:8080");

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(UserController.class)
                .getUserByUserId(user.getUserIdentifier().getUserId())
        ).withSelfRel().withHref(baseUri
                .path(WebMvcLinkBuilder.linkTo(
                                WebMvcLinkBuilder.methodOn(UserController.class)
                                        .getUserByUserId(user.getUserIdentifier().getUserId()))
                        .toUri().getPath())
                .toUriString());

        userResponseModel.add(selfLink);
    }
}

