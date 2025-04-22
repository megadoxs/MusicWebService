package com.champlain.songservice.presentationlayer;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PlaylistResponseModel extends RepresentationModel<PlaylistResponseModel> {
    private String identifier;
    private String name;
    private String user;
    private List<SongResponseModel> songs;

    @JsonSetter("user")
    public void unpackUserFromObject(Map<String, Object> user) {
        this.user = user.get("userId").toString();
    }
}
