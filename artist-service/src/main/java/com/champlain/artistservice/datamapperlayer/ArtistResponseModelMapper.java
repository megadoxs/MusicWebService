package com.champlain.artistservice.datamapperlayer;

import com.champlain.artistservice.dataaccesslayer.Artist;
import com.champlain.artistservice.presentationlayer.ArtistController;
import com.champlain.artistservice.presentationlayer.ArtistResponseModel;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistResponseModelMapper {
    @Mapping(target = "identifier", expression = "java(artist.getIdentifier().getArtistId())")
    ArtistResponseModel entityToResponseModel(Artist artist);

    List<ArtistResponseModel> entityToResponseModelList(List<Artist> artists);

    @AfterMapping
    default void addLinks(@MappingTarget ArtistResponseModel artistResponseModel, Artist artist) {
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(ArtistController.class)
                .getArtistById(artist.getIdentifier().getArtistId())
        ).withSelfRel();

        artistResponseModel.add(selfLink);
    }
}
