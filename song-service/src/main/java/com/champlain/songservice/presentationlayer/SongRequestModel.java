package com.champlain.songservice.presentationlayer;

import com.champlain.songservice.dataaccesslayer.Genre;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SongRequestModel {
    private List<String> artists;
    private String title;
    private Genre genre;
    private LocalDate releaseDate;
    private Time duration;
}
