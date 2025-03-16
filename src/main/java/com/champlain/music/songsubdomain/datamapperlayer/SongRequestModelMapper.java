package com.champlain.music.songsubdomain.datamapperlayer;

import com.champlain.music.songsubdomain.dataaccesslayer.Song;
import com.champlain.music.songsubdomain.presentationlayer.SongRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SongRequestModelMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "artists", expression = "java(song.getArtists().stream().map(id -> new com.champlain.music.songsubdomain.dataaccesslayer.ArtistIdentifier(id)).toList())")
    Song requestModelToEntity(SongRequestModel song);
}
