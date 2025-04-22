package com.champlain.songservice.presentationlayer;

import com.champlain.songservice.dataaccesslayer.Genre;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SongResponseModel extends RepresentationModel<SongResponseModel> {
    private String identifier;
    private List<ArtistResponseModel> artists;
    private String title;
    private Genre genre;
    private LocalDate releaseDate;
    private Time duration;
}
