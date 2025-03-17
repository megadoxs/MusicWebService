package com.champlain.music.playlistsubdomain.datamapperlayer;

import com.champlain.music.playlistsubdomain.dataaccesslayer.playlist.Playlist;
import com.champlain.music.playlistsubdomain.presentationlayer.PlaylistController;
import com.champlain.music.playlistsubdomain.presentationlayer.PlaylistResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaylistResponseModelMapper {
    @Mapping(target = "identifier", expression = "java(playlist.getIdentifier().getPlaylistId())")
    @Mapping(target = "songs", ignore = true)
    @Mapping(target = "user", ignore = true)
    PlaylistResponseModel entityToResponseModel(Playlist playlist);
    List<PlaylistResponseModel> entityToResponseModelList(List<Playlist> playlists);

    @AfterMapping
    default void addLinks(@MappingTarget PlaylistResponseModel playlistResponseModel, Playlist playlist) {
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(PlaylistController.class)
                .getPlaylistById(playlist.getIdentifier().getPlaylistId())
        ).withSelfRel();

        Link artistsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(PlaylistController.class)
                .getAllArtists(playlist.getIdentifier().getPlaylistId())
        ).withRel("artists");

        playlistResponseModel.add(selfLink);
        playlistResponseModel.add(artistsLink);
    }
}
