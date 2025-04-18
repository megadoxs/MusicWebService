package com.champlain.userservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlaylistResponseModel extends RepresentationModel<PlaylistResponseModel> {
    private String identifier;
    private UserResponseModel user;
}
