package com.champlain.music.artistsubdomain.datamapperlayer;

import com.champlain.music.artistsubdomain.dataaccesslayer.Artist;
import com.champlain.music.artistsubdomain.presentationlayer.ArtistResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistResponseModelMapper {
    @Mapping(target = "identifier", expression = "java(artist.getIdentifier().getArtistId())")
    ArtistResponseModel entityToResponseModel(Artist artist);
    List<ArtistResponseModel> entityToResponseModelList(List<Artist> artists);
}
