package com.champlain.songservice.datamapperlayer;

import com.champlain.songservice.dataaccesslayer.Song;
import com.champlain.songservice.presentationlayer.SongController;
import com.champlain.songservice.presentationlayer.SongResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SongResponseModelMapper {
    @Mapping(target = "identifier", expression = "java(song.getIdentifier().getSongId())")
    @Mapping(target = "artists", ignore = true)
    SongResponseModel entityToResponseModel(Song song);

    List<SongResponseModel> entityToResponseModelList(List<Song> songs);

    @AfterMapping
    default void addLinks(@MappingTarget SongResponseModel songResponseModel, Song song) {
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(SongController.class)
                .getSongById(song.getIdentifier().getSongId())
        ).withSelfRel();

        songResponseModel.add(selfLink);
    }
}
