package com.champlain.music.songsubdomain.presentationlayer;

import com.champlain.music.songsubdomain.dataaccesslayer.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SongResponseModel extends RepresentationModel<SongResponseModel> {
    private String identifier;
    private List<ArtistResponseModel> artists;
    private String title;
    private Genre genre;
    private LocalDate releaseDate;
    private Time duration;
}
