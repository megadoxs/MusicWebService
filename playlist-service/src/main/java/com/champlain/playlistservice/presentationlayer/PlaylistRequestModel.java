package com.champlain.playlistservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlaylistRequestModel {
    private String name;
    private String user;
    private List<String> songs;
}
