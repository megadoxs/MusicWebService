package com.champlain.apigatewayservice.presentationlayer.artistdto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ArtistResponseModel extends RepresentationModel<ArtistResponseModel> {
    private String identifier;
    private String firstName;
    private String lastName;
    private String stageName;
}
