package com.champlain.music.usersubdomain.datamapperlayer;

import ch.qos.logback.core.model.ComponentModel;
import com.champlain.music.usersubdomain.dataaccesslayer.User;
import com.champlain.music.usersubdomain.dataaccesslayer.UserIdentifier;
import com.champlain.music.usersubdomain.presentationlayer.UserRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserRequestMapper {

    @Mapping(target = "id", ignore = true)
    User requestModelToEntity(UserRequestModel customerRequestModel,
                              UserIdentifier customerIdentifier);
    List<User> requestModelListToEntityList(List<UserRequestModel> userRequestModel);

}
