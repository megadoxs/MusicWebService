package com.champlain.songservice.dataaccesslayer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class SongRepositoryIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder();
        }
    }

    private final String VALID_SONG_ID = "100e8400-e29b-41d4-a716-446655440010";
    private final String NOT_FOUND_SONG_ID = "100e8400-e29b-41d4-a716-44665544001";
    private final String VALID_SONG_NAME = "Shallow";
    private final String VALID_SONG_GENRE = "POP";
    private final LocalDate VALID_SONG_RELEASE_DATE =  LocalDate.parse("2018-09-27");
    private final Time VALID_SONG_DURATION = new Time(00, 03, 35);
    private final List<String> VALID_SONG_ARTIST_ID = List.of("550e8400-e29b-41d4-a716-446655440000");

    @Autowired
    private SongRepository songRepository;

    @Test
    public void whenCustomerDoesNotExist_ReturnNull() {
        //act
        Song customer = songRepository.findSongByIdentifier_SongId(NOT_FOUND_SONG_ID);

        //assert
        assertNull(customer);
    }

    @Test
    public void whenSongExist_ReturnSongById() {
        //act
        Song song = songRepository.findSongByIdentifier_SongId(VALID_SONG_ID);

        //assert
        assertNotNull(song);
        assertEquals(VALID_SONG_ID, song.getIdentifier().getSongId());
        assertEquals(VALID_SONG_NAME, song.getTitle());
        assertEquals(VALID_SONG_GENRE, song.getGenre().toString());
        assertEquals(VALID_SONG_RELEASE_DATE, song.getReleaseDate());
        assertEquals(VALID_SONG_DURATION, song.getDuration());
        assertArrayEquals(VALID_SONG_ARTIST_ID.toArray(), song.getArtists().toArray());
    }

    @Test
    public void whenSongsExist_ReturnAllSongs() {
        Long sizeDB = songRepository.count();

        //act
        List<Song> songs = songRepository.findAll();

        //assert
        assertEquals(sizeDB, songs.size());
    }


    @Test
    public void whenAddSong_ReturnSong(){
        Song song = new Song();
        song.setGenre(Genre.COUNTRY);
        song.setArtists(List.of("some artist"));
        song.setTitle("test");
        song.setDuration(Time.valueOf(LocalTime.of(1, 1, 1)));
        song.setReleaseDate(LocalDate.parse("2018-09-27"));
        song.setIdentifier(new SongIdentifier());

        Song savedSong = songRepository.save(song);

        assertNotNull(savedSong);
        assertEquals(song.getId(), savedSong.getId());
        assertEquals(song.getIdentifier().getSongId(), savedSong.getIdentifier().getSongId());
        assertEquals(song.getTitle(), savedSong.getTitle());
        assertEquals(song.getGenre(), savedSong.getGenre());
        assertEquals(song.getReleaseDate(), savedSong.getReleaseDate());
        assertEquals(song.getDuration(), savedSong.getDuration());
        assertArrayEquals(song.getArtists().toArray(), savedSong.getArtists().toArray());
    }

    @Test
    public void whenUpdateSong_ReturnSong(){
        Song foundSong = songRepository.findSongByIdentifier_SongId(VALID_SONG_ID);

        Song song = new Song();
        song.setGenre(Genre.COUNTRY);
        song.setArtists(List.of("some artist"));
        song.setTitle("test");
        song.setDuration(Time.valueOf(LocalTime.of(1, 1, 1)));
        song.setReleaseDate(LocalDate.parse("2018-09-27"));
        song.setIdentifier(foundSong.getIdentifier());
        song.setId(foundSong.getId());

        Song savedSong = songRepository.save(song);

        assertNotNull(savedSong);
        assertEquals(song.getId(), savedSong.getId());
        assertEquals(song.getIdentifier().getSongId(), savedSong.getIdentifier().getSongId());
        assertEquals(song.getTitle(), savedSong.getTitle());
        assertEquals(song.getGenre(), savedSong.getGenre());
        assertEquals(song.getReleaseDate(), savedSong.getReleaseDate());
        assertEquals(song.getDuration(), savedSong.getDuration());
        assertArrayEquals(song.getArtists().toArray(), savedSong.getArtists().toArray());
    }

    @Test
    public void whenDeleteSong_ReturnNull(){
        songRepository.delete(songRepository.findSongByIdentifier_SongId(VALID_SONG_ID));

        assertNull(songRepository.findSongByIdentifier_SongId(VALID_SONG_ID));
    }
}
