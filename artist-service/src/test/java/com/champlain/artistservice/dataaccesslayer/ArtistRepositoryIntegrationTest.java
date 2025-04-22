package com.champlain.artistservice.dataaccesslayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ArtistRepositoryIntegrationTest {
    private final String VALID_ARTIST_ID = "550e8400-e29b-41d4-a716-446655440000";
    private final String NOT_FOUND_ARTIST_ID = "100e8400-e29b-41d4-a716-44665544001";
    private final String VALID_ARTIST_FIRST_NAME = "Stefani";
    private final String VALID_ARTIST_LAST_NAME = "Germanotta";
    private final String VALID_ARTIST_STAGE_NAME = "Lady Gaga";

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    public void whenCustomerDoesNotExist_ReturnNull() {
        //act
        Artist customer = artistRepository.findArtistByIdentifier_ArtistId(NOT_FOUND_ARTIST_ID);

        //assert
        assertNull(customer);
    }

    @Test
    public void whenArtistExist_ReturnArtistById() {
        //act
        Artist artist = artistRepository.findArtistByIdentifier_ArtistId(VALID_ARTIST_ID);

        //assert
        assertNotNull(artist);
        assertEquals(VALID_ARTIST_ID, artist.getIdentifier().getArtistId());
        assertEquals(VALID_ARTIST_FIRST_NAME, artist.getFirstName());
        assertEquals(VALID_ARTIST_LAST_NAME, artist.getLastName());
        assertEquals(VALID_ARTIST_STAGE_NAME, artist.getStageName());
    }

    @Test
    public void whenArtistsExist_ReturnAllArtists() {
        Long sizeDB = artistRepository.count();

        //act
        List<Artist> artists = artistRepository.findAll();

        //assert
        assertEquals(sizeDB, artists.size());
    }
}
