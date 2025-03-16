package com.champlain.music.songsubdomain.datamapperlayer;

import com.champlain.music.songsubdomain.dataaccesslayer.Song;
import com.champlain.music.songsubdomain.presentationlayer.SongResponseModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SongResponseModelMapper {
    @Mapping(target = "identifier", expression = "java(song.getIdentifier().getSongId())")
    @Mapping(target = "artists", expression = "java(song.getArtists().stream().map(com.champlain.music.songsubdomain.dataaccesslayer.ArtistIdentifier::getArtistId).toList())")
    SongResponseModel entityToResponseModel(Song song);
    List<SongResponseModel> entityToResponseModelList(List<Song> songs);
}
