package com.champlain.music.songsubdomain.presentationlayer;

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
    private String LastName;
    private String stageName;
}
