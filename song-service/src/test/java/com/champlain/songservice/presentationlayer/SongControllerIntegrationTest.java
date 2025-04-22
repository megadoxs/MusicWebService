package com.champlain.songservice.presentationlayer;

import com.champlain.songservice.dataaccesslayer.Genre;
import com.champlain.songservice.dataaccesslayer.SongRepository;
import com.champlain.songservice.domainclientlayer.ArtistServiceClient;
import com.champlain.songservice.domainclientlayer.PlaylistServiceClient;
import com.champlain.songservice.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties  = {"spring.datasource.url=jdbc:h2:mem:song-db"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("h2")
public class SongControllerIntegrationTest {
    private final String BASE_URI_SONGS = "/api/v1/songs";
    private final String VALID_SONG_ID = "100e8400-e29b-41d4-a716-446655440010";
    private final String NOT_FOUND_SONG_ID = "100e8400-e29b-41d4-a716-44665544001";
    private final String VALID_SONG_NAME = "Shallow";
    private final String VALID_SONG_GENRE = "POP";
    private final LocalDate VALID_SONG_RELEASE_DATE =  LocalDate.parse("2018-09-27");
    private final Time VALID_SONG_DURATION = new Time(00, 03, 35);
    private final List<String> VALID_SONG_ARTIST_ID = List.of("550e8400-e29b-41d4-a716-446655440000");

    @Autowired
    private SongRepository songRepository;
    @MockitoBean
    private PlaylistServiceClient playlistServiceClient;
    @MockitoBean
    private ArtistServiceClient artistServiceClient;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenDeleteSong_thenDeleteSongSuccessfully() {
        // Act
        webTestClient.delete().uri(BASE_URI_SONGS + "/" + VALID_SONG_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        //Assert
        assertNull(songRepository.findSongByIdentifier_SongId(VALID_SONG_ID));
    }

    @Test
    public void whenDeleteSongAndOnlySongInAPlaylist_thenDeleteSongSuccessfully() {
        when(playlistServiceClient.getPlaylistsBySongId(VALID_SONG_ID)).thenReturn(List.of(PlaylistResponseModel.builder().songs(List.of(new SongResponseModel())).build()));
        // Act
        webTestClient.delete().uri(BASE_URI_SONGS + "/" + VALID_SONG_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        //Assert
        assertNull(songRepository.findSongByIdentifier_SongId(VALID_SONG_ID));
    }

    @Test
    public void whenDeleteSongAndManySongInAPlaylist_thenDeleteSongSuccessfully() {
        when(playlistServiceClient.getPlaylistsBySongId(VALID_SONG_ID)).thenReturn(List.of(PlaylistResponseModel.builder().songs(List.of(SongResponseModel.builder().identifier(VALID_SONG_ID).build(), SongResponseModel.builder().identifier("some other songId").build())).build()));
        // Act
        webTestClient.delete().uri(BASE_URI_SONGS + "/" + VALID_SONG_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        //Assert
        assertNull(songRepository.findSongByIdentifier_SongId(VALID_SONG_ID));
    }

    @Test
    public void whenRemoveNonExistentSong_thenThrowNotFoundException() {
        // Act & Assert
        webTestClient.delete().uri(BASE_URI_SONGS + "/" + NOT_FOUND_SONG_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("song with id " + NOT_FOUND_SONG_ID + " was not found");
    }

    @Test
    public void whenGetSongById_thenReturnSong() {
        when(artistServiceClient.getArtistById(VALID_SONG_ARTIST_ID.getFirst())).thenReturn(new ArtistResponseModel());
        // Act & Assert
        webTestClient.get().uri(BASE_URI_SONGS + "/" + VALID_SONG_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SongResponseModel.class)
                .value((Song) -> {
                    assertNotNull(Song);
                    assertEquals(VALID_SONG_ID, Song.getIdentifier());
                    assertEquals(VALID_SONG_NAME, Song.getTitle());
                    assertEquals(VALID_SONG_GENRE, Song.getGenre().toString());
                    assertEquals(VALID_SONG_RELEASE_DATE, Song.getReleaseDate());
                    assertEquals(VALID_SONG_DURATION, Song.getDuration());
                    assertNotNull(Song.getArtists().toArray());
                });
    }

    @Test
    public void whenGetInvalidSong_thenReturnThrowNotFoundException() {
        // Act & Assert
        webTestClient.get().uri(BASE_URI_SONGS + "/" + NOT_FOUND_SONG_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("song with id " + NOT_FOUND_SONG_ID + " was not found");
    }

    @Test
    public void whenValidSong_thenCreateSong(){
        //arrange
        long sizeDB = songRepository.count();
        when(artistServiceClient.getArtistById(VALID_SONG_ARTIST_ID.getFirst())).thenReturn(new ArtistResponseModel());

        SongRequestModel SongToCreate = SongRequestModel.builder()
                .title(VALID_SONG_NAME)
                .genre(Genre.valueOf(VALID_SONG_GENRE))
                .releaseDate(VALID_SONG_RELEASE_DATE)
                .duration(VALID_SONG_DURATION)
                .artists(VALID_SONG_ARTIST_ID)
                .build();

        webTestClient.post()
                .uri(BASE_URI_SONGS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(SongToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(SongResponseModel.class)
                .value((songResponseModel) -> {
                    assertNotNull(SongToCreate);
                    assertEquals(SongToCreate.getTitle(), songResponseModel.getTitle());
                    assertEquals(SongToCreate.getGenre(), songResponseModel.getGenre());
                    assertEquals(SongToCreate.getReleaseDate(), songResponseModel.getReleaseDate());
                    assertEquals(SongToCreate.getDuration(), songResponseModel.getDuration());
                    assertNotNull(songResponseModel.getArtists());
                });

        long sizeDBAfter = songRepository.count();
        assertEquals(sizeDB + 1, sizeDBAfter);
    }

    @Test
    public void whenAddSongWithInvalidArtistId_thenThrowNotFoundException() {
        //arrange
        when(artistServiceClient.getArtistById("some invalid artist id")).thenReturn(null);

        SongRequestModel SongToCreate = SongRequestModel.builder()
                .title(VALID_SONG_NAME)
                .genre(Genre.valueOf(VALID_SONG_GENRE))
                .releaseDate(VALID_SONG_RELEASE_DATE)
                .duration(VALID_SONG_DURATION)
                .artists(List.of("some invalid artist id"))
                .build();

        webTestClient.post()
                .uri(BASE_URI_SONGS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(SongToCreate)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("artist with id " + "some invalid artist id" + " was not found");

    }

    @Test
    public void whenUpdateNonExistentSong_thenThrowNotFoundException() {
        // Arrange
        SongRequestModel updatedSong = new SongRequestModel();

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_SONGS + "/" + NOT_FOUND_SONG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedSong)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("song with id " + NOT_FOUND_SONG_ID + " was not found");
    }

    @Test
    public void whenUpdateSong_thenReturnUpdatedSong() {
        // Arrange
        when(artistServiceClient.getArtistById(VALID_SONG_ARTIST_ID.getFirst())).thenReturn(new ArtistResponseModel());
        SongRequestModel SongToUpdate = SongRequestModel.builder()
                .title("new song name")
                .genre(Genre.valueOf(VALID_SONG_GENRE))
                .releaseDate(VALID_SONG_RELEASE_DATE)
                .duration(VALID_SONG_DURATION)
                .artists(VALID_SONG_ARTIST_ID)
                .build();

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_SONGS + "/" + VALID_SONG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(SongToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SongResponseModel.class)
                .value((updatedSong) -> {
                    assertNotNull(updatedSong);
                    assertEquals(VALID_SONG_ID, updatedSong.getIdentifier());
                    assertEquals("new song name", updatedSong.getTitle());
                    assertEquals(VALID_SONG_GENRE, updatedSong.getGenre().toString());
                    assertEquals(VALID_SONG_RELEASE_DATE, updatedSong.getReleaseDate());
                    assertEquals(VALID_SONG_DURATION, updatedSong.getDuration());
                    assertNotNull(updatedSong.getArtists());
                });
    }

    @Test
    public void whenUpdateSongWithInvalidArtistId_thenThrowNotFoundException() {
        //arrange
        when(artistServiceClient.getArtistById("some invalid artist id")).thenReturn(null);

        SongRequestModel SongToCreate = SongRequestModel.builder()
                .title(VALID_SONG_NAME)
                .genre(Genre.valueOf(VALID_SONG_GENRE))
                .releaseDate(VALID_SONG_RELEASE_DATE)
                .duration(VALID_SONG_DURATION)
                .artists(List.of("some invalid artist id"))
                .build();

        webTestClient.put()
                .uri(BASE_URI_SONGS + "/" + VALID_SONG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(SongToCreate)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("artist with id " + "some invalid artist id" + " was not found");

    }

    @Test
    public void whenAllSongs_thenReturnAllSongs() {
        webTestClient.get()
                .uri(BASE_URI_SONGS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SongResponseModel[].class);
    }

    @Test
    public void whenGetSongsByArtistId_thenReturnSongs() {
        webTestClient.get()
                .uri(BASE_URI_SONGS + "/artist/" + VALID_SONG_ARTIST_ID.getFirst())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(SongResponseModel[].class);
    }
}
