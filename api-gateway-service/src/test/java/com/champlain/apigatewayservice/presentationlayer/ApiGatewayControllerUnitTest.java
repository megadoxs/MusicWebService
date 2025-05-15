package com.champlain.apigatewayservice.presentationlayer;

import com.champlain.apigatewayservice.buisnesslogiclayer.ArtistService;
import com.champlain.apigatewayservice.buisnesslogiclayer.PlaylistService;
import com.champlain.apigatewayservice.buisnesslogiclayer.SongService;
import com.champlain.apigatewayservice.buisnesslogiclayer.UserService;
import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistResponseModel;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistResponseModel;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongRequestModel;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongResponseModel;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserRequestModel;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserResponseModel;
import com.champlain.apigatewayservice.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ArtistController.class, PlaylistController.class, SongController.class, UserController.class})
public class ApiGatewayControllerUnitTest {
    @MockitoBean
    PlaylistService playlistService;

    @Autowired
    PlaylistController playlistController;

    @MockitoBean
    ArtistService artistService;

    @Autowired
    ArtistController artistController;

    @MockitoBean
    SongService songService;

    @Autowired
    SongController songController;

    @MockitoBean
    UserService userService;

    @Autowired
    UserController userController;

    private PlaylistResponseModel getPlaylistResponseModel(){
        return PlaylistResponseModel.builder()
                .identifier("200e8400-e29b-41d4-a716-446655440020")
                .name("Workout Hits")
                .user(UserResponseModel.builder().build())
                .songs(List.of(getSongResponseModel()))
                .duration(Time.valueOf("00:08:01"))
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

    private ArtistResponseModel getArtistResponseModel(){
        return ArtistResponseModel.builder()
                .identifier("550e8400-e29b-41d4-a716-446655440000")
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

    @Test
    public void whenPlaylistExists_ThenReturnPlaylistById(){
        PlaylistResponseModel playlistResponseModel = getPlaylistResponseModel();

        when(playlistService.getPlaylistById("200e8400-e29b-41d4-a716-446655440020")).thenReturn(playlistResponseModel);
        ResponseEntity<PlaylistResponseModel> response = playlistController.getPlaylistById("200e8400-e29b-41d4-a716-446655440020");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playlistResponseModel, response.getBody());
        verify(playlistService, times(1)).getPlaylistById("200e8400-e29b-41d4-a716-446655440020");
    }

    @Test
    public void whenPlaylistIdInvalidOnGet_ThenThrowNotFoundException(){
        when(playlistService.getPlaylistById("invalid")).thenThrow(new NotFoundException("Playlist with id invalid not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> playlistController.getPlaylistById("invalid"));

        assertEquals("Playlist with id invalid not found", exception.getMessage());
        verify(playlistService, times(1)).getPlaylistById("invalid");
    }

    @Test
    public void whenSongExists_ThenReturnSongById(){
        SongResponseModel songResponseModel = getSongResponseModel();

        when(songService.getSongById("200e8400-e29b-41d4-a716-446655440020")).thenReturn(songResponseModel);
        ResponseEntity<SongResponseModel> response = songController.getSongById("200e8400-e29b-41d4-a716-446655440020");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(songResponseModel, response.getBody());
        verify(songService, times(1)).getSongById("200e8400-e29b-41d4-a716-446655440020");
    }

    @Test
    public void whenSongIdInvalidOnGet_ThenThrowNotFoundException(){
        when(songService.getSongById("invalid")).thenThrow(new NotFoundException("Song with id invalid not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> songController.getSongById("invalid"));

        assertEquals("Song with id invalid not found", exception.getMessage());
        verify(songService, times(1)).getSongById("invalid");
    }

    @Test
    public void whenArtistExists_ThenReturnSongById(){
        ArtistResponseModel artistResponseModel = getArtistResponseModel();

        when(artistService.getArtistById("200e8400-e29b-41d4-a716-446655440020")).thenReturn(artistResponseModel);
        ResponseEntity<ArtistResponseModel> response = artistController.getArtistById("200e8400-e29b-41d4-a716-446655440020");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(artistResponseModel, response.getBody());
        verify(artistService, times(1)).getArtistById("200e8400-e29b-41d4-a716-446655440020");
    }

    @Test
    public void whenArtistIdInvalidOnGet_ThenThrowNotFoundException(){
        when(artistService.getArtistById("invalid")).thenThrow(new NotFoundException("Artist with id invalid not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> artistController.getArtistById("invalid"));

        assertEquals("Artist with id invalid not found", exception.getMessage());
        verify(artistService, times(1)).getArtistById("invalid");
    }

    @Test
    public void whenUserExists_ThenReturnSongById(){
        UserResponseModel userResponseModel = getUserResponseModel();

        when(userService.getUserById("200e8400-e29b-41d4-a716-446655440020")).thenReturn(userResponseModel);
        ResponseEntity<UserResponseModel> response = userController.getUserById("200e8400-e29b-41d4-a716-446655440020");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userResponseModel, response.getBody());
        verify(userService, times(1)).getUserById("200e8400-e29b-41d4-a716-446655440020");
    }

    @Test
    public void whenUserIdInvalidOnGet_ThenThrowNotFoundException(){
        when(userService.getUserById("invalid")).thenThrow(new NotFoundException("User with id invalid not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userController.getUserById("invalid"));

        assertEquals("User with id invalid not found", exception.getMessage());
        verify(userService, times(1)).getUserById("invalid");
    }
}
