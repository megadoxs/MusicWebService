package com.champlain.apigatewayservice.presentationlayer.playlistdto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlaylistRequestModel {
    private String name;
    private String user;
    private List<String> songs;
}
