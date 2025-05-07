package com.champlain.playlistservice.presentationlayer;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Time;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlaylistResponseModel extends RepresentationModel<PlaylistResponseModel> {
    private String identifier;
    private String name;
    private UserResponseModel user;
    private List<SongResponseModel> songs;
    private Time duration;
}
