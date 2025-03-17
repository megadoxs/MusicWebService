package com.champlain.music.usersubdomain.datamapperlayer;

import com.champlain.music.usersubdomain.dataaccesslayer.User;
import com.champlain.music.usersubdomain.presentationlayer.UserRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userIdentifier", ignore = true)
    User requestModelToEntity(UserRequestModel customerRequestModel);
}
