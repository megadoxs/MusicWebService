package com.champlain.songservice.datamapperlayer;

import com.champlain.songservice.dataaccesslayer.Song;
import com.champlain.songservice.presentationlayer.SongRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongRequestModelMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "artists", expression = "java(song.getArtists())")
    Song requestModelToEntity(SongRequestModel song);
}
