package com.champlain.playlistservice.dataaccesslayer.playlist;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.List;

@Entity
@Data
@Table(name = "playlists")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private PlaylistIdentifier identifier;
    private String name;
    @Column(name = "user_id")
    private String user;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "playlist_songs", joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "song_id")
    private List<String> songs;
    private Time duration; // the invariant
}
