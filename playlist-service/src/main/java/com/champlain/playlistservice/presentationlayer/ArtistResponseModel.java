package com.champlain.playlistservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistResponseModel extends RepresentationModel<ArtistResponseModel> {
    private String identifier;
    private String firstName;
    private String lastName;
    private String stageName;
}
