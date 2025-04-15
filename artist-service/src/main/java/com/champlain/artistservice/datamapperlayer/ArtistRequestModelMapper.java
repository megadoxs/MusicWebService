package com.champlain.artistservice.datamapperlayer;

import com.champlain.artistservice.dataaccesslayer.Artist;
import com.champlain.artistservice.presentationlayer.ArtistRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistRequestModelMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identifier", ignore = true)
    Artist requestModelToEntity(ArtistRequestModel artistRequestModel);
}
