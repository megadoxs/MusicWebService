package com.champlain.music.playlistsubdomain.datamapperlayer;

import com.champlain.music.playlistsubdomain.dataaccesslayer.playlist.Playlist;
import com.champlain.music.playlistsubdomain.presentationlayer.PlaylistRequestModel;
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
