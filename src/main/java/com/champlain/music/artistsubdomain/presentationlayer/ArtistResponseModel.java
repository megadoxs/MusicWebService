package com.champlain.music.artistsubdomain.presentationlayer;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ArtistResponseModel {
    private String identifier;
    private String firstName;
    private String LastName;
    private String stageName;
}
