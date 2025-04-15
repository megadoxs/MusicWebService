package com.champlain.playlistservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Time;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlaylistResponseModel extends RepresentationModel<PlaylistResponseModel> {
    private String identifier;
    private String name;
    private UserResponseModel user;
    private List<SongResponseModel> songs;
    private Time duration;
}
