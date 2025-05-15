package com.champlain.apigatewayservice.presentationlayer;

import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistResponseModel;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistResponseModel;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongRequestModel;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongResponseModel;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserRequestModel;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserResponseModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
public class ApiGatewayControllerIntegrationTest {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    RestTemplate restTemplate;

    MockRestServiceServer mockRestServiceServer;

    @BeforeEach
    public void init() {
        mockRestServiceServer = MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
    }

    private PlaylistResponseModel getPlaylistResponseModel(){
        return PlaylistResponseModel.builder()
                .identifier("200e8400-e29b-41d4-a716-446655440020")
                .name("Workout Hits")
                .user(UserResponseModel.builder().build())
                .songs(List.of(getSongResponseModel()))
                .duration(Time.valueOf("00:08:01"))
                .build();
    }

    private PlaylistRequestModel getPlaylistRequestModel(){
        return PlaylistRequestModel.builder()
                .name("Workout Hits")
                .user("57659e81-7c6b-4711-bb9e-bc73e1fa7824")
                .songs(List.of("100e8400-e29b-41d4-a716-446655440010"))
                .build();
    }

    private SongResponseModel getSongResponseModel(){
        return SongResponseModel.builder()
                .identifier("100e8400-e29b-41d4-a716-446655440010")
                .title("Shallow")
                .genre("POP")
                .releaseDate(LocalDate.parse("2018-09-27"))
                .duration(Time.valueOf("00:03:35"))
                .artists(List.of(getArtistResponseModel()))
                .build();
    }

    private SongRequestModel getSongRequestModel(){
        return SongRequestModel.builder()
                .title("Shallow")
                .genre("POP")
                .releaseDate(LocalDate.parse("2018-09-27"))
                .duration(Time.valueOf("00:03:35"))
                .artists(List.of("550e8400-e29b-41d4-a716-446655440000"))
                .build();
    }

    private ArtistResponseModel getArtistResponseModel(){
        return ArtistResponseModel.builder()
                .identifier("550e8400-e29b-41d4-a716-446655440000")
                .firstName("Stefani")
                .lastName("Germanotta")
                .stageName("Lady Gaga")
                .build();
    }

    private ArtistRequestModel getArtistRequestModel(){
        return ArtistRequestModel.builder()
                .firstName("Stefani")
                .lastName("Germanotta")
                .stageName("Lady Gaga")
                .build();
    }

    private UserResponseModel getUserResponseModel(){
        return UserResponseModel.builder()
                .userId("57659e81-7c6b-4711-bb9e-bc73e1fa7824")
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();
    }

    private UserRequestModel getUserRequestModel(){
        return UserRequestModel.builder()
                .email("ethan.hunt@example.com")
                .firstName("Ethan")
                .lastName("Hunt")
                .username("ethanh85")
                .build();
    }

    @Test
    public void getAllPlaylists() throws Exception {
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(List.of(playlistResponseModel))));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/playlists")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].identifier").isEqualTo(playlistResponseModel.getIdentifier())
                .jsonPath("$[0].name").isEqualTo("Workout Hits")
                // User assertions
                .jsonPath("$[0].user.userId").isEqualTo(playlistResponseModel.getUser().getUserId())
                .jsonPath("$[0].user.username").isEqualTo(playlistResponseModel.getUser().getUsername())
                .jsonPath("$[0].user.lastName").isEqualTo(playlistResponseModel.getUser().getLastName())
                .jsonPath("$[0].user.firstName").isEqualTo(playlistResponseModel.getUser().getFirstName())
                .jsonPath("$[0].user.email").isEqualTo(playlistResponseModel.getUser().getEmail())
                // First song assertions
                .jsonPath("$[0].songs[0].identifier").isEqualTo(playlistResponseModel.getSongs().getFirst().getIdentifier())
                .jsonPath("$[0].songs[0].title").isEqualTo(playlistResponseModel.getSongs().getFirst().getTitle())
                .jsonPath("$[0].songs[0].genre").isEqualTo(playlistResponseModel.getSongs().getFirst().getGenre())
                .jsonPath("$[0].songs[0].releaseDate").isEqualTo(playlistResponseModel.getSongs().getFirst().getReleaseDate().toString())
                .jsonPath("$[0].songs[0].duration").isEqualTo(playlistResponseModel.getSongs().getFirst().getDuration().toString())
                // First artist assertions
                .jsonPath("$[0].songs[0].artists[0].identifier").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getIdentifier())
                .jsonPath("$[0].songs[0].artists[0].firstName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getFirstName())
                .jsonPath("$[0].songs[0].artists[0].lastName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getLastName())
                .jsonPath("$[0].songs[0].artists[0].stageName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getStageName())
                // Check playlist total duration
                .jsonPath("$[0].duration").isEqualTo("00:08:01");

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getPlaylist() throws Exception {
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists/" + playlistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(playlistResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/playlists/" + playlistResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(playlistResponseModel.getIdentifier())
                .jsonPath("$.name").isEqualTo("Workout Hits")
                // User assertions
                .jsonPath("$.user.userId").isEqualTo(playlistResponseModel.getUser().getUserId())
                .jsonPath("$.user.username").isEqualTo(playlistResponseModel.getUser().getUsername())
                .jsonPath("$.user.lastName").isEqualTo(playlistResponseModel.getUser().getLastName())
                .jsonPath("$.user.firstName").isEqualTo(playlistResponseModel.getUser().getFirstName())
                .jsonPath("$.user.email").isEqualTo(playlistResponseModel.getUser().getEmail())
                // First song assertions
                .jsonPath("$.songs[0].identifier").isEqualTo(playlistResponseModel.getSongs().getFirst().getIdentifier())
                .jsonPath("$.songs[0].title").isEqualTo(playlistResponseModel.getSongs().getFirst().getTitle())
                .jsonPath("$.songs[0].genre").isEqualTo(playlistResponseModel.getSongs().getFirst().getGenre())
                .jsonPath("$.songs[0].releaseDate").isEqualTo(playlistResponseModel.getSongs().getFirst().getReleaseDate().toString())
                .jsonPath("$.songs[0].duration").isEqualTo(playlistResponseModel.getSongs().getFirst().getDuration().toString())
                // First artist assertions
                .jsonPath("$.songs[0].artists[0].identifier").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getIdentifier())
                .jsonPath("$.songs[0].artists[0].firstName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getFirstName())
                .jsonPath("$.songs[0].artists[0].lastName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getLastName())
                .jsonPath("$.songs[0].artists[0].stageName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getStageName())
                // Check playlist total duration
                .jsonPath("$.duration").isEqualTo("00:08:01");

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void addPlaylist() throws Exception {
        PlaylistRequestModel playlistRequestModel = getPlaylistRequestModel();
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(playlistResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.post()
                .uri("/api/v1/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playlistRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(playlistResponseModel.getIdentifier())
                .jsonPath("$.name").isEqualTo("Workout Hits")
                // User assertions
                .jsonPath("$.user.userId").isEqualTo(playlistResponseModel.getUser().getUserId())
                .jsonPath("$.user.username").isEqualTo(playlistResponseModel.getUser().getUsername())
                .jsonPath("$.user.lastName").isEqualTo(playlistResponseModel.getUser().getLastName())
                .jsonPath("$.user.firstName").isEqualTo(playlistResponseModel.getUser().getFirstName())
                .jsonPath("$.user.email").isEqualTo(playlistResponseModel.getUser().getEmail())
                // First song assertions
                .jsonPath("$.songs[0].identifier").isEqualTo(playlistResponseModel.getSongs().getFirst().getIdentifier())
                .jsonPath("$.songs[0].title").isEqualTo(playlistResponseModel.getSongs().getFirst().getTitle())
                .jsonPath("$.songs[0].genre").isEqualTo(playlistResponseModel.getSongs().getFirst().getGenre())
                .jsonPath("$.songs[0].releaseDate").isEqualTo(playlistResponseModel.getSongs().getFirst().getReleaseDate().toString())
                .jsonPath("$.songs[0].duration").isEqualTo(playlistResponseModel.getSongs().getFirst().getDuration().toString())
                // First artist assertions
                .jsonPath("$.songs[0].artists[0].identifier").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getIdentifier())
                .jsonPath("$.songs[0].artists[0].firstName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getFirstName())
                .jsonPath("$.songs[0].artists[0].lastName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getLastName())
                .jsonPath("$.songs[0].artists[0].stageName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getStageName())
                // Check playlist total duration
                .jsonPath("$.duration").isEqualTo("00:08:01");

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void UpdatePlaylist() throws Exception {
        PlaylistRequestModel playlistRequestModel = getPlaylistRequestModel();
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists/" + playlistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(playlistResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.put()
                .uri("/api/v1/playlists/" + playlistResponseModel.getIdentifier())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playlistRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(playlistResponseModel.getIdentifier())
                .jsonPath("$.name").isEqualTo("Workout Hits")
                // User assertions
                .jsonPath("$.user.userId").isEqualTo(playlistResponseModel.getUser().getUserId())
                .jsonPath("$.user.username").isEqualTo(playlistResponseModel.getUser().getUsername())
                .jsonPath("$.user.lastName").isEqualTo(playlistResponseModel.getUser().getLastName())
                .jsonPath("$.user.firstName").isEqualTo(playlistResponseModel.getUser().getFirstName())
                .jsonPath("$.user.email").isEqualTo(playlistResponseModel.getUser().getEmail())
                // First song assertions
                .jsonPath("$.songs[0].identifier").isEqualTo(playlistResponseModel.getSongs().getFirst().getIdentifier())
                .jsonPath("$.songs[0].title").isEqualTo(playlistResponseModel.getSongs().getFirst().getTitle())
                .jsonPath("$.songs[0].genre").isEqualTo(playlistResponseModel.getSongs().getFirst().getGenre())
                .jsonPath("$.songs[0].releaseDate").isEqualTo(playlistResponseModel.getSongs().getFirst().getReleaseDate().toString())
                .jsonPath("$.songs[0].duration").isEqualTo(playlistResponseModel.getSongs().getFirst().getDuration().toString())
                // First artist assertions
                .jsonPath("$.songs[0].artists[0].identifier").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getIdentifier())
                .jsonPath("$.songs[0].artists[0].firstName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getFirstName())
                .jsonPath("$.songs[0].artists[0].lastName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getLastName())
                .jsonPath("$.songs[0].artists[0].stageName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getStageName())
                // Check playlist total duration
                .jsonPath("$.duration").isEqualTo("00:08:01");

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void deletePlaylist() throws Exception {
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists/" + playlistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        webTestClient.delete()
                .uri("/api/v1/playlists/" + playlistResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isNoContent();

        mockRestServiceServer.verify();
    }

    @Test
    public void getPlaylistArtists() throws Exception {
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();
        ArtistResponseModel artistResponseModel = getArtistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists/" + playlistResponseModel.getIdentifier() + "/artists")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(List.of(artistResponseModel))));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/playlists/" + playlistResponseModel.getIdentifier() + "/artists")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].identifier").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getIdentifier())
                .jsonPath("$[0].firstName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getFirstName())
                .jsonPath("$[0].lastName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getLastName())
                .jsonPath("$[0].stageName").isEqualTo(playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getStageName());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getPlaylistFail() throws Exception {
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists/" + playlistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/playlists/" + playlistResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void UpdatePlaylistFail() throws Exception {
        PlaylistRequestModel playlistRequestModel = getPlaylistRequestModel();
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists/" + playlistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // ACT + ASSERT with more asserts
        this.webTestClient.put()
                .uri("/api/v1/playlists/" + playlistResponseModel.getIdentifier())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playlistRequestModel)
                .exchange()
                .expectStatus().isBadRequest();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getPlaylistArtistsFail() throws Exception {
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists/" + playlistResponseModel.getIdentifier() + "/artists")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/playlists/" + playlistResponseModel.getIdentifier() + "/artists")
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void deletePlaylistFail() throws Exception {
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists/" + playlistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        webTestClient.delete()
                .uri("/api/v1/playlists/" + playlistResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isNotFound();

        mockRestServiceServer.verify();
    }

    @Test
    public void addPlaylistFail() throws Exception {
        PlaylistRequestModel playlistRequestModel = getPlaylistRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.post()
                .uri("/api/v1/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(playlistRequestModel)
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getAllPlaylistsFail() throws Exception {
        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/playlists")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/playlists")
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getAllSongs() throws Exception {
        SongResponseModel songResponseModel = getSongResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(List.of(songResponseModel))));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/songs")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].identifier").isEqualTo(songResponseModel.getIdentifier())
                .jsonPath("$[0].title").isEqualTo(songResponseModel.getTitle())
                .jsonPath("$[0].genre").isEqualTo(songResponseModel.getGenre())
                .jsonPath("$[0].releaseDate").isEqualTo(songResponseModel.getReleaseDate().toString())
                .jsonPath("$[0].duration").isEqualTo(songResponseModel.getDuration().toString())
                .jsonPath("$[0].artists[0].identifier").isEqualTo(songResponseModel.getArtists().getFirst().getIdentifier())
                .jsonPath("$[0].artists[0].firstName").isEqualTo(songResponseModel.getArtists().getFirst().getFirstName())
                .jsonPath("$[0].artists[0].lastName").isEqualTo(songResponseModel.getArtists().getFirst().getLastName())
                .jsonPath("$[0].artists[0].stageName").isEqualTo(songResponseModel.getArtists().getFirst().getStageName());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getSong() throws Exception {
        SongResponseModel songResponseModel = getSongResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs/" + songResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/songs/" + songResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(songResponseModel.getIdentifier())
                .jsonPath("$.title").isEqualTo(songResponseModel.getTitle())
                .jsonPath("$.genre").isEqualTo(songResponseModel.getGenre())
                .jsonPath("$.releaseDate").isEqualTo(songResponseModel.getReleaseDate().toString())
                .jsonPath("$.duration").isEqualTo(songResponseModel.getDuration().toString())
                .jsonPath("$.artists[0].identifier").isEqualTo(songResponseModel.getArtists().getFirst().getIdentifier())
                .jsonPath("$.artists[0].firstName").isEqualTo(songResponseModel.getArtists().getFirst().getFirstName())
                .jsonPath("$.artists[0].lastName").isEqualTo(songResponseModel.getArtists().getFirst().getLastName())
                .jsonPath("$.artists[0].stageName").isEqualTo(songResponseModel.getArtists().getFirst().getStageName());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void addSong() throws Exception {
        SongResponseModel songResponseModel = getSongResponseModel();
        SongRequestModel songRequestModel = getSongRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.post()
                .uri("/api/v1/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(songRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(songResponseModel.getIdentifier())
                .jsonPath("$.title").isEqualTo(songResponseModel.getTitle())
                .jsonPath("$.genre").isEqualTo(songResponseModel.getGenre())
                .jsonPath("$.releaseDate").isEqualTo(songResponseModel.getReleaseDate().toString())
                .jsonPath("$.duration").isEqualTo(songResponseModel.getDuration().toString())
                .jsonPath("$.artists[0].identifier").isEqualTo(songResponseModel.getArtists().getFirst().getIdentifier())
                .jsonPath("$.artists[0].firstName").isEqualTo(songResponseModel.getArtists().getFirst().getFirstName())
                .jsonPath("$.artists[0].lastName").isEqualTo(songResponseModel.getArtists().getFirst().getLastName())
                .jsonPath("$.artists[0].stageName").isEqualTo(songResponseModel.getArtists().getFirst().getStageName());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void UpdateSong() throws Exception {
        SongResponseModel songResponseModel = getSongResponseModel();
        SongRequestModel songRequestModel = getSongRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs/" + songResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.put()
                .uri("/api/v1/songs/" + songResponseModel.getIdentifier())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(songRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(songResponseModel.getIdentifier())
                .jsonPath("$.title").isEqualTo(songResponseModel.getTitle())
                .jsonPath("$.genre").isEqualTo(songResponseModel.getGenre())
                .jsonPath("$.releaseDate").isEqualTo(songResponseModel.getReleaseDate().toString())
                .jsonPath("$.duration").isEqualTo(songResponseModel.getDuration().toString())
                .jsonPath("$.artists[0].identifier").isEqualTo(songResponseModel.getArtists().getFirst().getIdentifier())
                .jsonPath("$.artists[0].firstName").isEqualTo(songResponseModel.getArtists().getFirst().getFirstName())
                .jsonPath("$.artists[0].lastName").isEqualTo(songResponseModel.getArtists().getFirst().getLastName())
                .jsonPath("$.artists[0].stageName").isEqualTo(songResponseModel.getArtists().getFirst().getStageName());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void deleteSong() throws Exception {
        SongResponseModel songResponseModel = getSongResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs/" + songResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        webTestClient.delete()
                .uri("/api/v1/songs/" + songResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isNoContent();

        mockRestServiceServer.verify();
    }

    @Test
    public void getAllSongsFail() throws Exception {
        SongResponseModel songResponseModel = getSongResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/songs")
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getSongFail() throws Exception {
        SongResponseModel songResponseModel = getSongResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs/" + songResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/songs/" + songResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void addSongFail() throws Exception {
        SongRequestModel songRequestModel = getSongRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.post()
                .uri("/api/v1/songs")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(songRequestModel)
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void UpdateSongFail() throws Exception {
        SongResponseModel songResponseModel = getSongResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs/" + songResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // ACT + ASSERT with more asserts
        this.webTestClient.put()
                .uri("/api/v1/songs/" + songResponseModel.getIdentifier())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getSongRequestModel())
                .exchange()
                .expectStatus().isBadRequest();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void deleteSongFail() throws Exception {
        SongResponseModel songResponseModel = getSongResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/songs/" + songResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        webTestClient.delete()
                .uri("/api/v1/songs/" + songResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isNotFound();

        mockRestServiceServer.verify();
    }

    @Test
    public void getAllArtists() throws Exception {
        ArtistResponseModel artistResponseModel = getArtistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(List.of(artistResponseModel))));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/artists")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].identifier").isEqualTo(artistResponseModel.getIdentifier())
                .jsonPath("$[0].firstName").isEqualTo(artistResponseModel.getFirstName())
                .jsonPath("$[0].lastName").isEqualTo(artistResponseModel.getLastName())
                .jsonPath("$[0].stageName").isEqualTo(artistResponseModel.getStageName());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getArtist() throws Exception {
        ArtistResponseModel artistResponseModel = getArtistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists/" + artistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(artistResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/artists/" + artistResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(artistResponseModel.getIdentifier())
                .jsonPath("$.firstName").isEqualTo(artistResponseModel.getFirstName())
                .jsonPath("$.lastName").isEqualTo(artistResponseModel.getLastName())
                .jsonPath("$.stageName").isEqualTo(artistResponseModel.getStageName());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void addArtist() throws Exception {
        ArtistResponseModel artistResponseModel = getArtistResponseModel();
        ArtistRequestModel artistRequestModel = getArtistRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(artistResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.post()
                .uri("/api/v1/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(artistRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(artistResponseModel.getIdentifier())
                .jsonPath("$.firstName").isEqualTo(artistResponseModel.getFirstName())
                .jsonPath("$.lastName").isEqualTo(artistResponseModel.getLastName())
                .jsonPath("$.stageName").isEqualTo(artistResponseModel.getStageName());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void UpdateArtist() throws Exception {
        ArtistResponseModel artistResponseModel = getArtistResponseModel();
        ArtistRequestModel artistRequestModel = getArtistRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists/" + artistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(artistResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.put()
                .uri("/api/v1/artists/" + artistResponseModel.getIdentifier())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(artistRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.identifier").isEqualTo(artistResponseModel.getIdentifier())
                .jsonPath("$.firstName").isEqualTo(artistResponseModel.getFirstName())
                .jsonPath("$.lastName").isEqualTo(artistResponseModel.getLastName())
                .jsonPath("$.stageName").isEqualTo(artistResponseModel.getStageName());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void deleteArtist() throws Exception {
        ArtistResponseModel artistResponseModel = getArtistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists/" + artistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        webTestClient.delete()
                .uri("/api/v1/artists/" + artistResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isNoContent();

        mockRestServiceServer.verify();
    }

    @Test
    public void getAllArtistsFail() throws Exception {
        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/artists")
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getArtistFail() throws Exception {
        ArtistResponseModel artistResponseModel = getArtistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists/" + artistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/artists/" + artistResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void addArtistFail() throws Exception {
        ArtistRequestModel artistRequestModel = getArtistRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.post()
                .uri("/api/v1/artists")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(artistRequestModel)
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void UpdateArtistFail() throws Exception {
        ArtistResponseModel artistResponseModel = getArtistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists/" + artistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // ACT + ASSERT with more asserts
        this.webTestClient.put()
                .uri("/api/v1/artists/" + artistResponseModel.getIdentifier())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getArtistRequestModel())
                .exchange()
                .expectStatus().isBadRequest();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void deleteArtistFail() throws Exception {
        ArtistResponseModel artistResponseModel = getArtistResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/artists/" + artistResponseModel.getIdentifier())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        webTestClient.delete()
                .uri("/api/v1/artists/" + artistResponseModel.getIdentifier())
                .exchange()
                .expectStatus().isNotFound();

        mockRestServiceServer.verify();
    }

    @Test
    public void getAllUsers() throws Exception {
        UserResponseModel userResponseModel = getUserResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(List.of(userResponseModel))));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].userId").isEqualTo(userResponseModel.getUserId())
                .jsonPath("$[0].username").isEqualTo(userResponseModel.getUsername())
                .jsonPath("$[0].lastName").isEqualTo(userResponseModel.getLastName())
                .jsonPath("$[0].firstName").isEqualTo(userResponseModel.getFirstName())
                .jsonPath("$[0].email").isEqualTo(userResponseModel.getEmail());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getUser() throws Exception {
        UserResponseModel userResponseModel = getUserResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users/" + userResponseModel.getUserId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(userResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/users/" + userResponseModel.getUserId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo(userResponseModel.getUserId())
                .jsonPath("$.username").isEqualTo(userResponseModel.getUsername())
                .jsonPath("$.lastName").isEqualTo(userResponseModel.getLastName())
                .jsonPath("$.firstName").isEqualTo(userResponseModel.getFirstName())
                .jsonPath("$.email").isEqualTo(userResponseModel.getEmail());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void addUser() throws Exception {
        UserResponseModel userResponseModel = getUserResponseModel();
        UserRequestModel userRequestModel = getUserRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(userResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestModel)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.userId").isEqualTo(userResponseModel.getUserId())
                .jsonPath("$.username").isEqualTo(userResponseModel.getUsername())
                .jsonPath("$.lastName").isEqualTo(userResponseModel.getLastName())
                .jsonPath("$.firstName").isEqualTo(userResponseModel.getFirstName())
                .jsonPath("$.email").isEqualTo(userResponseModel.getEmail());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void UpdateUser() throws Exception {
        UserResponseModel userResponseModel = getUserResponseModel();
        UserRequestModel userRequestModel = getUserRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users/" + userResponseModel.getUserId())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(userResponseModel)));

        // ACT + ASSERT with more asserts
        this.webTestClient.put()
                .uri("/api/v1/users/" + userResponseModel.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestModel)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userId").isEqualTo(userResponseModel.getUserId())
                .jsonPath("$.username").isEqualTo(userResponseModel.getUsername())
                .jsonPath("$.lastName").isEqualTo(userResponseModel.getLastName())
                .jsonPath("$.firstName").isEqualTo(userResponseModel.getFirstName())
                .jsonPath("$.email").isEqualTo(userResponseModel.getEmail());

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void deleteUser() throws Exception {
        UserResponseModel userResponseModel = getUserResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users/" + userResponseModel.getUserId())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        webTestClient.delete()
                .uri("/api/v1/users/" + userResponseModel.getUserId())
                .exchange()
                .expectStatus().isNoContent();

        mockRestServiceServer.verify();
    }

    @Test
    public void getAllUsersFail() throws Exception {
        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/users")
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void getUserFail() throws Exception {
        UserResponseModel userResponseModel = getUserResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users/" + userResponseModel.getUserId())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.get()
                .uri("/api/v1/users/" + userResponseModel.getUserId())
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void addUserFail() throws Exception {
        UserRequestModel userRequestModel = getUserRequestModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        // ACT + ASSERT with more asserts
        this.webTestClient.post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequestModel)
                .exchange()
                .expectStatus().isNotFound();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void UpdateUserFail() throws Exception {
        UserResponseModel userResponseModel = getUserResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users/" + userResponseModel.getUserId())))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST));

        // ACT + ASSERT with more asserts
        this.webTestClient.put()
                .uri("/api/v1/users/" + userResponseModel.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(getUserRequestModel())
                .exchange()
                .expectStatus().isBadRequest();

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

    @Test
    public void deleteUserFail() throws Exception {
        UserResponseModel userResponseModel = getUserResponseModel();

        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI("http://localhost:8080/api/v1/users/" + userResponseModel.getUserId())))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        webTestClient.delete()
                .uri("/api/v1/users/" + userResponseModel.getUserId())
                .exchange()
                .expectStatus().isNotFound();

        mockRestServiceServer.verify();
    }
}
