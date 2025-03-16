package com.champlain.music.artistsubdomain.presentationlayer;

import lombok.*;
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
