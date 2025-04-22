package com.champlain.artistservice.presentationlayer;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ArtistRequestModel {
    private String firstName;
    private String lastName;
    private String stageName;
}
