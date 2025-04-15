package com.champlain.songservice.dataaccesslayer;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
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
    @Column(name = "artist_id")
    private List<String> artists;
    private String title;
    @Enumerated(EnumType.STRING)
    private Genre genre;
    private LocalDate releaseDate;
    private Time duration;
}
