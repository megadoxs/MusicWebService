package com.champlain.music.artistsubdomain.dataaccesslayer;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "artists")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Embedded
    private ArtistIdentifier identifier;
    private String firstName;
    private String lastName;
    private String stageName;
}
