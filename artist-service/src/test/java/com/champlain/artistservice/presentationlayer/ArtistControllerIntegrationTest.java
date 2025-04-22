package com.champlain.artistservice.presentationlayer;

import com.champlain.artistservice.dataaccesslayer.ArtistRepository;
import com.champlain.artistservice.domainclientlayer.SongServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties  = {"spring.datasource.url=jdbc:h2:mem:artist-db"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("h2")
public class ArtistControllerIntegrationTest {
    private final String BASE_URI_ARTISTS = "/api/v1/artists";
    private final String VALID_ARTIST_ID = "550e8400-e29b-41d4-a716-446655440000";
    private final String NOT_FOUND_ARTIST_ID = "100e8400-e29b-41d4-a716-44665544001";
    private final String VALID_ARTIST_FIRST_NAME = "Stefani";
    private final String VALID_ARTIST_LAST_NAME = "Germanotta";
    private final String VALID_ARTIST_STAGE_NAME = "Lady Gaga";

    @Autowired
    private ArtistRepository artistRepository;
    @MockitoBean
    private SongServiceClient songServiceClient;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenDeleteArtist_thenDeleteArtistSuccessfully() {
        // Act
        webTestClient.delete().uri(BASE_URI_ARTISTS + "/" + VALID_ARTIST_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        //Assert
        assertNull(artistRepository.findArtistByIdentifier_ArtistId(VALID_ARTIST_ID));
    }

    @Test
    public void whenDeleteArtistAndOnlyArtistInASong_thenDeleteArtistSuccessfully() {
        when(songServiceClient.getSongsByArtistId(VALID_ARTIST_ID)).thenReturn(List.of(SongResponseModel.builder().artists(List.of(new ArtistResponseModel())).build()));
        // Act
        webTestClient.delete().uri(BASE_URI_ARTISTS + "/" + VALID_ARTIST_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        //Assert
        assertNull(artistRepository.findArtistByIdentifier_ArtistId(VALID_ARTIST_ID));
    }

    @Test
    public void whenDeleteArtistAndManyArtistInASong_thenDeleteArtistSuccessfully() {
        when(songServiceClient.getSongsByArtistId(VALID_ARTIST_ID)).thenReturn(List.of(SongResponseModel.builder().artists(List.of(ArtistResponseModel.builder().identifier(VALID_ARTIST_ID).build(), ArtistResponseModel.builder().identifier("some other artistId").build())).build()));
        // Act
        webTestClient.delete().uri(BASE_URI_ARTISTS + "/" + VALID_ARTIST_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        //Assert
        assertNull(artistRepository.findArtistByIdentifier_ArtistId(VALID_ARTIST_ID));
    }

    @Test
    public void whenRemoveNonExistentArtist_thenThrowNotFoundException() {
        // Act & Assert
        webTestClient.delete().uri(BASE_URI_ARTISTS + "/" + NOT_FOUND_ARTIST_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("artist with id " + NOT_FOUND_ARTIST_ID + " was not found");
    }

    @Test
    public void whenGetArtistById_thenReturnArtist() {
        // Act & Assert
        webTestClient.get().uri(BASE_URI_ARTISTS + "/" + VALID_ARTIST_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtistResponseModel.class)
                .value((Artist) -> {
                    assertNotNull(Artist);
                    assertEquals(VALID_ARTIST_ID, Artist.getIdentifier());
                    assertEquals(VALID_ARTIST_FIRST_NAME, Artist.getFirstName());
                    assertEquals(VALID_ARTIST_LAST_NAME, Artist.getLastName());
                    assertEquals(VALID_ARTIST_STAGE_NAME, Artist.getStageName());
                });
    }

    @Test
    public void whenGetInvalidArtist_thenReturnThrowNotFoundException() {
        // Act & Assert
        webTestClient.get().uri(BASE_URI_ARTISTS + "/" + NOT_FOUND_ARTIST_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("artist with id " + NOT_FOUND_ARTIST_ID + " was not found");
    }

    @Test
    public void whenValidArtist_thenCreateArtist(){
        //arrange
        long sizeDB = artistRepository.count();

        ArtistRequestModel ArtistToCreate = ArtistRequestModel.builder()
                .firstName(VALID_ARTIST_FIRST_NAME)
                .lastName(VALID_ARTIST_LAST_NAME)
                .stageName("test123")
                .build();

        webTestClient.post()
                .uri(BASE_URI_ARTISTS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ArtistToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ArtistResponseModel.class)
                .value((artistResponseModel) -> {
                    assertNotNull(ArtistToCreate);
                    assertEquals(ArtistToCreate.getFirstName(), artistResponseModel.getFirstName());
                    assertEquals(ArtistToCreate.getLastName(), artistResponseModel.getLastName());
                    assertEquals(ArtistToCreate.getStageName(), artistResponseModel.getStageName());
                });

        long sizeDBAfter = artistRepository.count();
        assertEquals(sizeDB + 1, sizeDBAfter);
    }

    @Test
    public void whenAddArtistWithDuplicateStageName_thenThrowDuplicateStageNameException() {
        ArtistRequestModel ArtistToCreate = ArtistRequestModel.builder()
                .firstName(VALID_ARTIST_FIRST_NAME)
                .lastName(VALID_ARTIST_LAST_NAME)
                .stageName(VALID_ARTIST_STAGE_NAME)
                .build();

        webTestClient.post()
                .uri(BASE_URI_ARTISTS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ArtistToCreate)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("Stage name " + VALID_ARTIST_STAGE_NAME + " already exists.");
    }

    @Test
    public void whenUpdateNonExistentArtist_thenThrowNotFoundException() {
        // Arrange
        ArtistRequestModel updatedArtist = new ArtistRequestModel();

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_ARTISTS + "/" + NOT_FOUND_ARTIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedArtist)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("artist with id " + NOT_FOUND_ARTIST_ID + " was not found");
    }

    @Test
    public void whenUpdateArtist_thenReturnUpdatedArtist() {
        // Arrange
        ArtistRequestModel ArtistToUpdate = ArtistRequestModel.builder()
                .firstName("some new name")
                .lastName(VALID_ARTIST_LAST_NAME)
                .stageName(VALID_ARTIST_STAGE_NAME)
                .build();

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_ARTISTS + "/" + VALID_ARTIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ArtistToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtistResponseModel.class)
                .value((updatedArtist) -> {
                    assertNotNull(updatedArtist);
                    assertEquals(VALID_ARTIST_ID, updatedArtist.getIdentifier());
                    assertEquals("some new name", updatedArtist.getFirstName());
                    assertEquals(VALID_ARTIST_LAST_NAME, updatedArtist.getLastName());
                    assertEquals(VALID_ARTIST_STAGE_NAME, updatedArtist.getStageName());
                });
    }

    @Test
    public void whenUpdateArtistWithDuplicateStageName_thenThrowDuplicateStageNameException() {
        ArtistRequestModel ArtistToUpdate = ArtistRequestModel.builder()
                .firstName(VALID_ARTIST_FIRST_NAME)
                .lastName(VALID_ARTIST_LAST_NAME)
                .stageName("Eminem")
                .build();

        webTestClient.put()
                .uri(BASE_URI_ARTISTS + "/" + VALID_ARTIST_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ArtistToUpdate)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("Stage name " + "Eminem" + " already exists.");
    }

    @Test
    public void whenAllArtists_thenReturnAllArtists() {
        webTestClient.get()
                .uri(BASE_URI_ARTISTS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ArtistResponseModel[].class);
    }
}
