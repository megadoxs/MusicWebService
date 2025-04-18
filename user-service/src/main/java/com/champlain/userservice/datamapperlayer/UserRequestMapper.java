package com.champlain.userservice.datamapperlayer;

import com.champlain.userservice.dataaccesslayer.User;
import com.champlain.userservice.presentationlayer.UserRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userIdentifier", ignore = true)
    @Mapping(target = "password", ignore = true)
    User requestModelToEntity(UserRequestModel customerRequestModel);
}
