package com.champlain.music.songsubdomain.dataaccesslayer;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "songs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private SongIdentifier identifier;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "song_artists", joinColumns = @JoinColumn(name = "song_id"))
    private List<ArtistIdentifier> artists;
    private String title;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private LocalDate releaseDate;
}
