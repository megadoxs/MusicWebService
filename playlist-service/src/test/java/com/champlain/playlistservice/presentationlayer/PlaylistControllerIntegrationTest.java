package com.champlain.playlistservice.presentationlayer;

import com.champlain.playlistservice.PlaylistServiceApplication;
import com.champlain.playlistservice.dataaccesslayer.playlist.PlaylistRepository;
import com.champlain.playlistservice.dataaccesslayer.song.Genre;
import com.champlain.playlistservice.presentationlayer.ArtistResponseModel;
import com.champlain.playlistservice.presentationlayer.SongResponseModel;
import com.champlain.playlistservice.presentationlayer.UserResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.datasource.url=jdbc:h2:mem:playlist-db"})
@ActiveProfiles("h2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PlaylistControllerIntegrationTest {


    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockRestServiceServer;
    @BeforeEach
    public void init() {
    //Attach MockRestServiceServer to the RestTemplate used in your client.
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
//        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);

    }

    @Test
    @Order(1)
    public void testGetPlaylists() throws Exception {
        // ARRANGE step
        String playlistId = "200e8400-e29b-41d4-a716-446655440020";
        String songId0 = "100e8400-e29b-41d4-a716-446655440010";
        String artistId0 = "550e8400-e29b-41d4-a716-446655440000";
        String userId = "57659e81-7c6b-4711-bb9e-bc73e1fa7824";
        String releaseDateString = "2018-09-27";

        ArrayList<ArtistResponseModel> artists0 = new ArrayList<>();
        ArtistResponseModel artistResponseModel = ArtistResponseModel.builder()
                .identifier(artistId0)
                .firstName("Stefani")
                .lastName("Germanotta")
                .stageName("Lady Gaga")
                .build();
        artists0.add(artistResponseModel);

        SongResponseModel songResponseModel0 = SongResponseModel.builder()
                .identifier(songId0)
                .title("Shallow")
                .genre(Genre.POP)
                .releaseDate(LocalDate.parse(releaseDateString))
                .duration(Time.valueOf("00:03:35"))
                .artists(artists0)
                .build();

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId(userId)
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();

        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(), requestTo(matchesPattern("http://localhost:8080/api/v1/songs/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel0)));

        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(), requestTo(matchesPattern("http://localhost:8080/api/v1/users/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(userResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/playlists")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].identifier").isEqualTo(playlistId)
                .jsonPath("$[0].name").isEqualTo("Workout Hits")
                // User assertions
                .jsonPath("$[0].user.userId").isEqualTo(userResponseModel.getUserId())
                .jsonPath("$[0].user.username").isEqualTo(userResponseModel.getUsername())
                .jsonPath("$[0].user.lastName").isEqualTo(userResponseModel.getLastName())
                .jsonPath("$[0].user.firstName").isEqualTo(userResponseModel.getFirstName())
                .jsonPath("$[0].user.email").isEqualTo(userResponseModel.getEmail())
                // First song assertions
                .jsonPath("$[0].songs[0].identifier").isEqualTo(songResponseModel0.getIdentifier())
                .jsonPath("$[0].songs[0].title").isEqualTo(songResponseModel0.getTitle())
                .jsonPath("$[0].songs[0].genre").isEqualTo(songResponseModel0.getGenre().toString())
                .jsonPath("$[0].songs[0].releaseDate[0]").isEqualTo(2018)
                .jsonPath("$[0].songs[0].releaseDate[1]").isEqualTo(9)
                .jsonPath("$[0].songs[0].releaseDate[2]").isEqualTo(27)
                .jsonPath("$[0].songs[0].duration").isEqualTo(songResponseModel0.getDuration().toString())
                // Check playlist total duration
                .jsonPath("$[0].duration").isEqualTo("00:08:01");

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    @Order(2)
    public void testGetPlaylistById() throws Exception {
        // ARRANGE step
        String playlistId = "200e8400-e29b-41d4-a716-446655440020";
        String songId0 = "100e8400-e29b-41d4-a716-446655440010";
        String songId1 = "100e8400-e29b-41d4-a716-446655440011";
        String artistId0 = "550e8400-e29b-41d4-a716-446655440000";
        String userId = "57659e81-7c6b-4711-bb9e-bc73e1fa7824";
        String url;
        String releaseDateString = "2018-09-27";

        ArrayList<ArtistResponseModel> artists0 = new ArrayList<>();
        ArtistResponseModel artistResponseModel = ArtistResponseModel.builder()
                .identifier(artistId0)
                .firstName("Stefani")
                .lastName("Germanotta")
                .stageName("Lady Gaga")
                .build();
        artists0.add(artistResponseModel);

        ArrayList<ArtistResponseModel> artists1 = new ArrayList<>();
        ArtistResponseModel artistResponseModel1 = ArtistResponseModel.builder()
                .identifier("550e8400-e29b-41d4-a716-446655440001")
                .firstName("Marshall")
                .lastName("Mathers")
                .stageName("Eminem")
                .build();
        artists1.add(artistResponseModel1);


        SongResponseModel songResponseModel0 = SongResponseModel.builder()
                .identifier(songId0)
                .title("Shallow")
                .genre(Genre.POP)
                .releaseDate(LocalDate.parse(releaseDateString))
                .duration(Time.valueOf("00:03:35"))
                .artists(artists0)
                .build();

        SongResponseModel songResponseModel1 = SongResponseModel.builder()
                .identifier(songId1)
                .title("Lose Yourself")
                .genre(Genre.HIP_HOP)
                .releaseDate(LocalDate.parse("2002-10-28"))
                .duration(Time.valueOf("00:05:26"))
                .artists(artists1)
                .build();

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId(userId)
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();

        // MOCK song-service
        url = "http://localhost:8080/api/v1/songs/" + songId0;
        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel0)));

        // MOCK song-service
        url = "http://localhost:8080/api/v1/songs/" + songId1;
        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel1)));

        // MOCK playlist-service
        url = "http://localhost:8080/api/v1/users/" + userId;
        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(userResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/playlists/" + playlistId)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(playlistId)
                .jsonPath("$.name").isEqualTo("Workout Hits")
                // User assertions
                .jsonPath("$.user.userId").isEqualTo(userResponseModel.getUserId())
                .jsonPath("$.user.username").isEqualTo(userResponseModel.getUsername())
                .jsonPath("$.user.lastName").isEqualTo(userResponseModel.getLastName())
                .jsonPath("$.user.firstName").isEqualTo(userResponseModel.getFirstName())
                .jsonPath("$.user.email").isEqualTo(userResponseModel.getEmail())
                // First song assertions
                .jsonPath("$.songs[0].identifier").isEqualTo(songResponseModel0.getIdentifier())
                .jsonPath("$.songs[0].title").isEqualTo(songResponseModel0.getTitle())
                .jsonPath("$.songs[0].genre").isEqualTo(songResponseModel0.getGenre().toString())
                .jsonPath("$.songs[0].releaseDate[0]").isEqualTo(2018)
                .jsonPath("$.songs[0].releaseDate[1]").isEqualTo(9)
                .jsonPath("$.songs[0].releaseDate[2]").isEqualTo(27)
                .jsonPath("$.songs[0].duration").isEqualTo(songResponseModel0.getDuration().toString())

                .jsonPath("$.songs[1].identifier").isEqualTo(songResponseModel1.getIdentifier())
                .jsonPath("$.songs[1].title").isEqualTo(songResponseModel1.getTitle())
                .jsonPath("$.songs[1].genre").isEqualTo(songResponseModel1.getGenre().toString())
                .jsonPath("$.songs[1].releaseDate[0]").isEqualTo(2002)
                .jsonPath("$.songs[1].releaseDate[1]").isEqualTo(10)
                .jsonPath("$.songs[1].releaseDate[2]").isEqualTo(28)
                .jsonPath("$.songs[1].duration").isEqualTo(songResponseModel1.getDuration().toString())
                // Check duration
                .jsonPath("$.duration").isEqualTo("00:08:01");

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    @Order(3)
    public void testAddPlaylist() throws Exception {
        String songId = "100e8400-e29b-41d4-a716-446655440010";
        String userId = "57659e81-7c6b-4711-bb9e-bc73e1fa7824";

        PlaylistRequestModel request = PlaylistRequestModel.builder()
                .name("Morning Boost")
                .songs(List.of(songId))
                .user(userId)
                .build();

        SongResponseModel song = SongResponseModel.builder()
                .identifier(songId)
                .title("Shallow")
                .genre(Genre.POP)
                .releaseDate(LocalDate.of(2018, 9, 27))
                .duration(Time.valueOf("00:03:35"))
                .artists(List.of(ArtistResponseModel.builder()
                        .identifier("550e8400-e29b-41d4-a716-446655440000")
                        .firstName("Stefani")
                        .lastName("Germanotta")
                        .stageName("Lady Gaga")
                        .build()))
                .build();

        UserResponseModel user = UserResponseModel.builder()
                .userId(userId)
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();

        mockRestServiceServer.expect(ExpectedCount.times(2),
                        requestTo("http://localhost:8080/api/v1/songs/" + songId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(song), MediaType.APPLICATION_JSON));

        mockRestServiceServer.expect(ExpectedCount.times(2),
                        requestTo("http://localhost:8080/api/v1/users/" + userId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(user), MediaType.APPLICATION_JSON));

        webTestClient.post()
                .uri("/api/v1/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Morning Boost")
                .jsonPath("$.songs[0].identifier").isEqualTo(songId)
                .jsonPath("$.user.userId").isEqualTo(userId);

        mockRestServiceServer.verify();
    }

    @Test
    public void testAddPlaylist_InvalidSongID() throws Exception {
        String songId = "100e8400-e29b-41d4-a716-446655440010";
        String userId = "57659e81-7c6b-4711-bb9e-bc73e1fa7824";

        PlaylistRequestModel request = PlaylistRequestModel.builder()
                .name("Morning Boost")
                .songs(List.of(songId))
                .user(userId)
                .build();

        UserResponseModel user = UserResponseModel.builder()
                .userId(userId)
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();

        mockRestServiceServer.expect(ExpectedCount.once(),
                        requestTo("http://localhost:8080/api/v1/songs/" + songId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND)
                        .body("song with id " + songId + " was not found")
                        .contentType(MediaType.APPLICATION_JSON));

        webTestClient.post()
                .uri("/api/v1/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(body -> assertTrue(body.contains("song with id " + songId + " was not found")));

        mockRestServiceServer.verify();
    }

    @Test
    @Order(4)
    public void testUpdatePlaylist() throws Exception {
        String songId = "100e8400-e29b-41d4-a716-446655440010";
        String userId = "57659e81-7c6b-4711-bb9e-bc73e1fa7824";

        PlaylistRequestModel request = PlaylistRequestModel.builder()
                .name("Morning Boost")
                .songs(List.of(songId))
                .user(userId)
                .build();

        SongResponseModel song = SongResponseModel.builder()
                .identifier(songId)
                .title("Shallow")
                .genre(Genre.POP)
                .releaseDate(LocalDate.of(2018, 9, 27))
                .duration(Time.valueOf("00:03:35"))
                .artists(List.of(ArtistResponseModel.builder()
                        .identifier("550e8400-e29b-41d4-a716-446655440000")
                        .firstName("Stefani")
                        .lastName("Germanotta")
                        .stageName("Lady Gaga")
                        .build()))
                .build();

        UserResponseModel user = UserResponseModel.builder()
                .userId(userId)
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();

        // Expect only 1 call to each external service
        mockRestServiceServer.expect(ExpectedCount.twice(),
                        requestTo("http://localhost:8080/api/v1/songs/" + songId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(song), MediaType.APPLICATION_JSON));

        mockRestServiceServer.expect(ExpectedCount.twice(),
                        requestTo("http://localhost:8080/api/v1/users/" + userId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(mapper.writeValueAsString(user), MediaType.APPLICATION_JSON));

        webTestClient.put()
                .uri("/api/v1/playlists/200e8400-e29b-41d4-a716-446655440020")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Morning Boost")
                .jsonPath("$.songs[0].identifier").isEqualTo(songId)
                .jsonPath("$.user.userId").isEqualTo(userId);

        mockRestServiceServer.verify();
    }

    @Test
    @Order(5)
    public void testDeletePlaylist() throws Exception {
        String playlistId = "200e8400-e29b-41d4-a716-446655440020";

        webTestClient.delete()
                .uri("/api/v1/playlists/" + playlistId)
                .exchange()
                .expectStatus().isNoContent();

        mockRestServiceServer.verify();
    }

    @Test
    @Order(1)
    public void testGetPlaylistByUser() throws Exception {
        // ARRANGE step
        String playlistId = "200e8400-e29b-41d4-a716-446655440020";
        String songId0 = "100e8400-e29b-41d4-a716-446655440010";
        String artistId0 = "550e8400-e29b-41d4-a716-446655440000";
        String userId = "57659e81-7c6b-4711-bb9e-bc73e1fa7824";
        String releaseDateString = "2018-09-27";

        ArrayList<ArtistResponseModel> artists0 = new ArrayList<>();
        ArtistResponseModel artistResponseModel = ArtistResponseModel.builder()
                .identifier(artistId0)
                .firstName("Stefani")
                .lastName("Germanotta")
                .stageName("Lady Gaga")
                .build();
        artists0.add(artistResponseModel);

        SongResponseModel songResponseModel0 = SongResponseModel.builder()
                .identifier(songId0)
                .title("Shallow")
                .genre(Genre.POP)
                .releaseDate(LocalDate.parse(releaseDateString))
                .duration(Time.valueOf("00:03:35"))
                .artists(artists0)
                .build();

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId(userId)
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();

        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(), requestTo(matchesPattern("http://localhost:8080/api/v1/songs/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel0)));

        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(), requestTo(matchesPattern("http://localhost:8080/api/v1/users/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(userResponseModel)));

        webTestClient.get()
                .uri("/api/v1/playlists/user/57659e81-7c6b-4711-bb9e-bc73e1fa7824")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].identifier").isEqualTo(playlistId)
                .jsonPath("$[0].name").isEqualTo("Workout Hits")
                // User assertions
                .jsonPath("$[0].user.userId").isEqualTo(userResponseModel.getUserId())
                .jsonPath("$[0].user.username").isEqualTo(userResponseModel.getUsername())
                .jsonPath("$[0].user.lastName").isEqualTo(userResponseModel.getLastName())
                .jsonPath("$[0].user.firstName").isEqualTo(userResponseModel.getFirstName())
                .jsonPath("$[0].user.email").isEqualTo(userResponseModel.getEmail())
                // First song assertions
                .jsonPath("$[0].songs[0].identifier").isEqualTo(songResponseModel0.getIdentifier())
                .jsonPath("$[0].songs[0].title").isEqualTo(songResponseModel0.getTitle())
                .jsonPath("$[0].songs[0].genre").isEqualTo(songResponseModel0.getGenre().toString())
                .jsonPath("$[0].songs[0].releaseDate[0]").isEqualTo(2018)
                .jsonPath("$[0].songs[0].releaseDate[1]").isEqualTo(9)
                .jsonPath("$[0].songs[0].releaseDate[2]").isEqualTo(27)
                .jsonPath("$[0].songs[0].duration").isEqualTo(songResponseModel0.getDuration().toString())
                // Check playlist total duration
                .jsonPath("$[0].duration").isEqualTo("00:08:01");

        mockRestServiceServer.verify();
    }

    @Test
    @Order(1)
    public void testGetPlaylistBySong() throws Exception {
        // ARRANGE step
        String playlistId = "200e8400-e29b-41d4-a716-446655440020";
        String songId0 = "100e8400-e29b-41d4-a716-446655440010";
        String artistId0 = "550e8400-e29b-41d4-a716-446655440000";
        String userId = "57659e81-7c6b-4711-bb9e-bc73e1fa7824";
        String releaseDateString = "2018-09-27";

        ArrayList<ArtistResponseModel> artists0 = new ArrayList<>();
        ArtistResponseModel artistResponseModel = ArtistResponseModel.builder()
                .identifier(artistId0)
                .firstName("Stefani")
                .lastName("Germanotta")
                .stageName("Lady Gaga")
                .build();
        artists0.add(artistResponseModel);

        SongResponseModel songResponseModel0 = SongResponseModel.builder()
                .identifier(songId0)
                .title("Shallow")
                .genre(Genre.POP)
                .releaseDate(LocalDate.parse(releaseDateString))
                .duration(Time.valueOf("00:03:35"))
                .artists(artists0)
                .build();

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId(userId)
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();

        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(), requestTo(matchesPattern("http://localhost:8080/api/v1/songs/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel0)));

        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(), requestTo(matchesPattern("http://localhost:8080/api/v1/users/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(userResponseModel)));

        webTestClient.get()
                .uri("/api/v1/playlists/song/100e8400-e29b-41d4-a716-446655440010")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].identifier").isEqualTo(playlistId)
                .jsonPath("$[0].name").isEqualTo("Workout Hits")
                // User assertions
                .jsonPath("$[0].user.userId").isEqualTo(userResponseModel.getUserId())
                .jsonPath("$[0].user.username").isEqualTo(userResponseModel.getUsername())
                .jsonPath("$[0].user.lastName").isEqualTo(userResponseModel.getLastName())
                .jsonPath("$[0].user.firstName").isEqualTo(userResponseModel.getFirstName())
                .jsonPath("$[0].user.email").isEqualTo(userResponseModel.getEmail())
                // First song assertions
                .jsonPath("$[0].songs[0].identifier").isEqualTo(songResponseModel0.getIdentifier())
                .jsonPath("$[0].songs[0].title").isEqualTo(songResponseModel0.getTitle())
                .jsonPath("$[0].songs[0].genre").isEqualTo(songResponseModel0.getGenre().toString())
                .jsonPath("$[0].songs[0].releaseDate[0]").isEqualTo(2018)
                .jsonPath("$[0].songs[0].releaseDate[1]").isEqualTo(9)
                .jsonPath("$[0].songs[0].releaseDate[2]").isEqualTo(27)
                .jsonPath("$[0].songs[0].duration").isEqualTo(songResponseModel0.getDuration().toString())
                // Check playlist total duration
                .jsonPath("$[0].duration").isEqualTo("00:08:01");

        mockRestServiceServer.verify();
    }

    @Test
    @Order(1)
    public void testGetPlaylistArtists() throws Exception {
        // ARRANGE step
        String playlistId = "200e8400-e29b-41d4-a716-446655440020";
        String songId0 = "100e8400-e29b-41d4-a716-446655440010";
        String artistId0 = "550e8400-e29b-41d4-a716-446655440000";
        String userId = "57659e81-7c6b-4711-bb9e-bc73e1fa7824";
        String releaseDateString = "2018-09-27";

        ArrayList<ArtistResponseModel> artists0 = new ArrayList<>();
        ArtistResponseModel artistResponseModel = ArtistResponseModel.builder()
                .identifier(artistId0)
                .firstName("Stefani")
                .lastName("Germanotta")
                .stageName("Lady Gaga")
                .build();
        artists0.add(artistResponseModel);

        SongResponseModel songResponseModel0 = SongResponseModel.builder()
                .identifier(songId0)
                .title("Shallow")
                .genre(Genre.POP)
                .releaseDate(LocalDate.parse(releaseDateString))
                .duration(Time.valueOf("00:03:35"))
                .artists(artists0)
                .build();

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId(userId)
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();

        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(), requestTo(matchesPattern("http://localhost:8080/api/v1/songs/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel0)));

        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(), requestTo(matchesPattern("http://localhost:8080/api/v1/users/.*")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(userResponseModel)));

        webTestClient.get()
                .uri("/api/v1/playlists/" + playlistId + "/artists")
                .exchange()
                .expectStatus().isOk()
                .expectBody();

        mockRestServiceServer.verify();
    }
}
