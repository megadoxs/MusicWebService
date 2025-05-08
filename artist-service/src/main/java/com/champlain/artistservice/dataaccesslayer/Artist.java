package com.champlain.artistservice.dataaccesslayer;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "artists")
@NoArgsConstructor
@Getter
@Setter
public class Artist {
    @Id
    private String id;
    private ArtistIdentifier identifier;
    private String firstName;
    private String lastName;
    private String stageName;

    public Artist(ArtistIdentifier identifier, String firstName, String lastName, String stageName) {
        this.identifier = identifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.stageName = stageName;
    }
}
