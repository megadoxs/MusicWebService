package com.champlain.apigatewayservice.presentationlayer.songdto;

import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistResponseModel;
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
    private String genre;
    private LocalDate releaseDate;
    private Time duration;
}
