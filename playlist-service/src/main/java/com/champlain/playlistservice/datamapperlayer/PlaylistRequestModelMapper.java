package com.champlain.playlistservice.datamapperlayer;

import com.champlain.playlistservice.dataaccesslayer.playlist.Playlist;
import com.champlain.playlistservice.presentationlayer.PlaylistRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlaylistRequestModelMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "identifier", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "user", expression = "java(playlist.getUser())")
    @Mapping(target = "songs", expression = "java(playlist.getSongs())")
    Playlist requestModelToEntity(PlaylistRequestModel playlist);
}
