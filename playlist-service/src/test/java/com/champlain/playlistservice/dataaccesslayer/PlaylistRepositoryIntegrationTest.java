package com.champlain.playlistservice.dataaccesslayer;

import com.champlain.playlistservice.dataaccesslayer.playlist.Playlist;
import com.champlain.playlistservice.dataaccesslayer.playlist.PlaylistIdentifier;
import com.champlain.playlistservice.dataaccesslayer.playlist.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class PlaylistRepositoryIntegrationTest {
    private final String VALID_PLAYLIST_ID = "200e8400-e29b-41d4-a716-446655440020";
    private final String NOT_FOUND_PLAYLIST_ID = "100e8400-e29b-41d4-a716-44665544001";
    private final String VALID_PLAYLIST_NAME = "Workout Hits";
    private final Time VALID_PLAYLIST_DURATION = new Time(0, 8, 1);
    private final String VALID_PLAYLIST_USER_ID = "57659e81-7c6b-4711-bb9e-bc73e1fa7824";
    private final List<String> VALID_PLAYLIST_SONG_IDS = List.of("100e8400-e29b-41d4-a716-446655440010", "100e8400-e29b-41d4-a716-446655440011");

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder();
        }
    }

    @Autowired
    PlaylistRepository playlistRepository;

    @Test
    public void whenPlaylistExist_ReturnPlaylistById() {
        //act
        Playlist playlist = playlistRepository.findByIdentifier_PlaylistId(VALID_PLAYLIST_ID);

        //assert
        assertNotNull(playlist);
        assertEquals(VALID_PLAYLIST_ID, playlist.getIdentifier().getPlaylistId());
        assertEquals(VALID_PLAYLIST_NAME, playlist.getName());
        assertEquals(VALID_PLAYLIST_DURATION, playlist.getDuration());
        assertEquals(VALID_PLAYLIST_USER_ID, playlist.getUser());
        assertEquals(VALID_PLAYLIST_SONG_IDS, playlist.getSongs());
    }

    @Test
    public void whenPlaylistNotExist_ReturnPlaylistById() {
        //act
        Playlist playlist = playlistRepository.findByIdentifier_PlaylistId(NOT_FOUND_PLAYLIST_ID);

        //assert
        assertNull(playlist);
    }

    @Test
    public void whenPlaylistsExist_ReturnAllPlaylists() {
        Long sizeDB = playlistRepository.count();

        //act
        List<Playlist> playlists = playlistRepository.findAll();

        //assert
        assertEquals(sizeDB, playlists.size());
    }

    @Test
    public void whenAddPlaylist_ReturnPlaylist(){
        Playlist playlist = Playlist.builder()
                .identifier(new PlaylistIdentifier())
                .name("new playlist")
                .user("57659e81-7c6b-4711-bb9e-bc73e1fa7824")
                .songs(VALID_PLAYLIST_SONG_IDS)
                .duration(Time.valueOf(LocalTime.of(0, 9, 1)))
                .build();

        Playlist savedPlaylist = playlistRepository.save(playlist);

        assertNotNull(savedPlaylist);
        assertEquals(playlist.getId(), savedPlaylist.getId());
        assertEquals(playlist.getIdentifier().getPlaylistId(), savedPlaylist.getIdentifier().getPlaylistId());
        assertEquals(playlist.getName(), savedPlaylist.getName());
        assertEquals(playlist.getUser(), savedPlaylist.getUser());
        assertEquals(playlist.getSongs(), savedPlaylist.getSongs());
        assertEquals(playlist.getDuration(), savedPlaylist.getDuration());
    }

    @Test
    public void whenUpdatePlaylist_ReturnPlaylist(){
        Playlist playlist = Playlist.builder()
                .identifier(new PlaylistIdentifier())
                .name("new playlist")
                .user("57659e81-7c6b-4711-bb9e-bc73e1fa7824")
                .songs(VALID_PLAYLIST_SONG_IDS)
                .duration(Time.valueOf(LocalTime.of(0, 9, 1)))
                .build();

        Playlist savedPlaylist = playlistRepository.save(playlist);

        assertNotNull(savedPlaylist);
        assertEquals(playlist.getId(), savedPlaylist.getId());
        assertEquals(playlist.getIdentifier().getPlaylistId(), savedPlaylist.getIdentifier().getPlaylistId());
        assertEquals(playlist.getName(), savedPlaylist.getName());
        assertEquals(playlist.getUser(), savedPlaylist.getUser());
        assertEquals(playlist.getSongs(), savedPlaylist.getSongs());
        assertEquals(playlist.getDuration(), savedPlaylist.getDuration());
    }

    @Test
    public void whenDeletePlaylist_ReturnNull(){
        playlistRepository.delete(playlistRepository.findByIdentifier_PlaylistId(VALID_PLAYLIST_ID));

        assertNull(playlistRepository.findByIdentifier_PlaylistId(VALID_PLAYLIST_ID));
    }

    @Test
    public void whenPlaylistNotExist_ReturnNull(){
        assertNull(playlistRepository.findByIdentifier_PlaylistId(NOT_FOUND_PLAYLIST_ID));
    }
}
