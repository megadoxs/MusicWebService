package com.champlain.music.artistsubdomain.datamapperlayer;

import com.champlain.music.artistsubdomain.dataaccesslayer.Artist;
import com.champlain.music.artistsubdomain.presentationlayer.ArtistRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArtistRequestModelMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identifier", ignore = true)
    Artist requestModelToEntity(ArtistRequestModel artistRequestModel);
}
