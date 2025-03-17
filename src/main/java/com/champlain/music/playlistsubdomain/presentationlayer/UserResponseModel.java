package com.champlain.music.playlistsubdomain.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseModel extends RepresentationModel<SongResponseModel> {
    private String userId;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
}
