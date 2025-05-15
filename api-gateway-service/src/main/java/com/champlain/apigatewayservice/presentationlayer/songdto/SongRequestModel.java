package com.champlain.apigatewayservice.presentationlayer.songdto;

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
    private String genre;
    private LocalDate releaseDate;
    private Time duration;
}
