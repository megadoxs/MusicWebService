package com.champlain.artistservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SongResponseModel extends RepresentationModel<SongResponseModel> {
    private String identifier;
    private List<ArtistResponseModel> artists;
    private String title;
    private String genre;
    private LocalDate releaseDate;
    private Time duration;
}
