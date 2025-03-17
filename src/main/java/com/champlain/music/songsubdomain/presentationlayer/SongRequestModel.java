package com.champlain.music.songsubdomain.presentationlayer;

import com.champlain.music.songsubdomain.dataaccesslayer.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SongRequestModel {
    private List<String> artists;
    private String title;
    private Genre genre;
    private LocalDate releaseDate;
    private Time duration;
}
