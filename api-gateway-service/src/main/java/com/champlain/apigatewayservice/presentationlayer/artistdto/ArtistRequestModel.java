package com.champlain.apigatewayservice.presentationlayer.artistdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistRequestModel {
    private String firstName;
    private String lastName;
    private String stageName;
}
