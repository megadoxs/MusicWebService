package com.champlain.playlistservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseModel extends RepresentationModel<SongResponseModel> {
    private String userId;
    private String username;
    private String lastName;
    private String firstName;
    private String email;
}
