package com.champlain.playlistservice.presentationlayer;

import com.champlain.playlistservice.dataaccesslayer.playlist.PlaylistRepository;
import com.champlain.playlistservice.dataaccesslayer.song.Genre;
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
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

//@SpringBootTest()
@SpringBootTest(webEnvironment = RANDOM_PORT,
 properties = {"spring.datasource.url=jdbc:h2:mem:playlists-db"})
//@Sql({"/data-h2.sql"})
@ActiveProfiles("h2")
public class PlaylistControllerIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PlaylistRepository playlistRepository;

    private final ObjectMapper mapper = new ObjectMapper();
    private MockRestServiceServer mockRestServiceServer;
    @BeforeEach
    public void init() {
//Attach MockRestServiceServer to the RestTemplate used in your client.
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testGetAllPlaylist() throws Exception {
        // ARRANGE step
        String playlistId = "123e4567-e89b-12d3-a456-556642440000";
        String songId = "100e8400-e29b-41d4-a716-446655440010";
        String artistId = "550e8400-e29b-41d4-a716-446655440000";
        String userId = "c27242a2-abb9-45b2-a85d-ed9ffa15f92c";
        String url;


        List<ArtistResponseModel> artists = new ArrayList<>();
    ArtistResponseModel artistResponseModel = new ArtistResponseModel(
            artistId, "Lady Gaga", "lady.gaga@example.com", "1986-03-28");
        SongResponseModel songResponseModel =
                new SongResponseModel(songId, artists, "title", Genre.COUNTRY,
                        LocalDate.of(2018, 9, 27), new Time(3 * 60 + 35 * 1000));
//

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId("c27242a2-abb9-45b2-a85d-ed9ffa15f92c")
                .email("alice.johnson@example.com")
                .firstName("Alice")
                .lastName("Johnson")
                .username("alicej95")
                .build();

        List<SongResponseModel> songs = new ArrayList<>();

        PlaylistResponseModel playlistResponseModel = PlaylistResponseModel.builder()
                .identifier(playlistId)
                .name("Top Hits")
                .user(userResponseModel)
                .songs(songs)
                .build();

        // MOCK song-service
        url = "http://localhost:8080/api/v1/songs/" + songId;
        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(songResponseModel)));

        // MOCK artist-service
        url = "http://localhost:8080/api/v1/artists/" + artistId;
        this.mockRestServiceServer.expect(ExpectedCount.once(), requestTo(new URI(url)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(artistResponseModel)));

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
                .jsonPath("$.playlistId").isEqualTo(playlistId)
                .jsonPath("$.name").isEqualTo("Top Hits")
                .jsonPath("$.description").isEqualTo("A collection of top hit songs")
                .jsonPath("$.songs[0].title").isEqualTo("Shallow")
                .jsonPath("$.songs[0].genre").isEqualTo("POP")
                .jsonPath("$.songs[0].releaseDate").isEqualTo("2018-09-27")
                .jsonPath("$.songs[0].duration").isEqualTo("00:03:35")
                .jsonPath("$.songs[0].artist.name").isEqualTo("Lady Gaga");

//                    artistId, "Lady Gaga", "lady.gaga@example.com", "1986-03-28");

        // VERIFY that the RestTemplate calls were made as expected
        this.mockRestServiceServer.verify();
    }

}
