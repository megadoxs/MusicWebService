package com.champlain.userservice.presentationlayer;

import com.champlain.userservice.dataaccesslayer.UserRepository;
import com.champlain.userservice.domainclientlayer.PlaylistServiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties  = {"spring.datasource.url=jdbc:h2:mem:user-db"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("h2")
public class UserControllerIntegrationTest {
    private final String BASE_URI_USERS = "/api/v1/users";
    private final String VALID_USER_ID = "c27242a2-abb9-45b2-a85d-ed9ffa15f92c";
    private final String NOT_FOUND_USER_ID = "c3540a89-cb47-4c96-888eff96708db4d0";
    private final String VALID_USER_FIRST_NAME = "Alice";
    private final String VALID_USER_LAST_NAME = "Johnson";
    private final String VALID_USER_EMAIL =  "alice.johnson@example.com";

    @Autowired
    private UserRepository userRepository;
    @MockitoBean
    private PlaylistServiceClient playlistServiceClient;
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void whenDeleteUser_thenDeleteUserSuccessfully() {
        // Act
        webTestClient.delete().uri(BASE_URI_USERS + "/" + VALID_USER_ID)
                .exchange()
                .expectStatus()
                .isNoContent();

        //Assert
        assertNull(userRepository.findUserByUserIdentifier_UserId(VALID_USER_ID));
    }

    @Test
    public void whenRemoveNonExistentUser_thenThrowNotFoundException() {
        // Act & Assert
        webTestClient.delete().uri(BASE_URI_USERS + "/" + NOT_FOUND_USER_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("User with userId: " + NOT_FOUND_USER_ID + " not found.");
    }

    @Test
    public void whenGetUserById_thenReturnUser() {
        // Act & Assert
        webTestClient.get().uri(BASE_URI_USERS + "/" + VALID_USER_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseModel.class)
                .value((User) -> {
                    assertNotNull(User);
                    assertEquals(VALID_USER_ID, User.getUserId());
                    assertEquals(VALID_USER_FIRST_NAME, User.getFirstName());
                    assertEquals(VALID_USER_LAST_NAME, User.getLastName());
                    assertEquals(VALID_USER_EMAIL, User.getEmail());
                });
    }

    @Test
    public void whenGetInvalidUser_thenReturnThrowNotFoundException() {
        // Act & Assert
        webTestClient.get().uri(BASE_URI_USERS + "/" + NOT_FOUND_USER_ID)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("User with userId: " + NOT_FOUND_USER_ID + " not found.");
    }

    @Test
    public void whenValidUser_thenCreateUser(){
        //arrange
        long sizeDB = userRepository.count();

        UserRequestModel UserToCreate = UserRequestModel.builder()
                .firstName("test")
                .lastName("test")
                .username("test")
                .dateOfBirth(new Date())
                .email("test@example.com")
                .password1("test")
                .password2("test")
                .build();

        webTestClient.post()
                .uri(BASE_URI_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserToCreate)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseModel.class)
                .value((userResponseModel) -> {
                    assertNotNull(UserToCreate);
                    assertEquals(UserToCreate.getFirstName(), userResponseModel.getFirstName());
                    assertEquals(UserToCreate.getEmail(), userResponseModel.getEmail());
                });

        long sizeDBAfter = userRepository.count();
        assertEquals(sizeDB + 1, sizeDBAfter);
    }

    @Test
    public void whenAddUserWithNullPasswords_thenReturnUpdatedUser() {
        // Arrange
        UserRequestModel UserToAdd = UserRequestModel.builder()
                .firstName("test")
                .lastName("test")
                .username("test")
                .dateOfBirth(new Date())
                .email("test@example.com")
                .build();

        // Act & Assert
        webTestClient.post()
                .uri(BASE_URI_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserToAdd)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("password can't be empty.");
    }

    @Test
    public void whenAddUserWithMismatchedPasswords_thenReturnUpdatedUser() {
        // Arrange
        UserRequestModel UserToAdd = UserRequestModel.builder()
                .firstName("test")
                .lastName("test")
                .username("test")
                .dateOfBirth(new Date())
                .email("test@example.com")
                .password1("test")
                .password2("test2")
                .build();

        // Act & Assert
        webTestClient.post()
                .uri(BASE_URI_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserToAdd)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("passwords do not match.");
    }

    @Test
    public void whenUpdateNonExistentUser_thenThrowNotFoundException() {
        // Arrange
        UserRequestModel updatedUser = new UserRequestModel();

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_USERS + "/" + NOT_FOUND_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedUser)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("NOT_FOUND")
                .jsonPath("$.message").isEqualTo("User with userId: " + NOT_FOUND_USER_ID + " not found.");
    }

    @Test
    public void whenUpdateUser_thenReturnUpdatedUser() {
        // Arrange
        UserRequestModel UserToUpdate = UserRequestModel.builder()
                .firstName("test")
                .lastName("test")
                .username("test")
                .dateOfBirth(new Date())
                .email("test@example.com")
                .password1("test")
                .password2("test")
                .build();

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_USERS + "/" + VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserToUpdate)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseModel.class)
                .value((updatedUser) -> {
                    assertNotNull(updatedUser);
                    assertEquals(UserToUpdate.getFirstName(), updatedUser.getFirstName());
                    assertEquals(UserToUpdate.getLastName(), updatedUser.getLastName());
                    assertEquals(UserToUpdate.getEmail(), updatedUser.getEmail());
                });
    }

    @Test
    public void whenUpdateUserWithNullPasswords_thenReturnUpdatedUser() {
        // Arrange
        UserRequestModel UserToUpdate = UserRequestModel.builder()
                .firstName("test")
                .lastName("test")
                .username("test")
                .dateOfBirth(new Date())
                .email("test@example.com")
                .build();

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_USERS + "/" + VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserToUpdate)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("password can't be empty.");
    }

    @Test
    public void whenUpdateUserWithMismatchedPasswords_thenReturnUpdatedUser() {
        // Arrange
        UserRequestModel UserToUpdate = UserRequestModel.builder()
                .firstName("test")
                .lastName("test")
                .username("test")
                .dateOfBirth(new Date())
                .email("test@example.com")
                .password1("test")
                .password2("test2")
                .build();

        // Act & Assert
        webTestClient.put()
                .uri(BASE_URI_USERS + "/" + VALID_USER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(UserToUpdate)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.httpStatus").isEqualTo("BAD_REQUEST")
                .jsonPath("$.message").isEqualTo("passwords do not match.");
    }

    @Test
    public void whenAllUsers_thenReturnAllUsers() {
        webTestClient.get()
                .uri(BASE_URI_USERS)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseModel[].class);
    }
}
