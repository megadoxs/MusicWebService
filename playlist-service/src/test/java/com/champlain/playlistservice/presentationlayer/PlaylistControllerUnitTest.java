package com.champlain.playlistservice.presentationlayer;

import com.champlain.playlistservice.buisneelogiclayer.PlaylistService;
import com.champlain.playlistservice.dataaccesslayer.playlist.PlaylistIdentifier;
import com.champlain.playlistservice.utils.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PlaylistController.class)
public class PlaylistControllerUnitTest {
    @MockitoBean
    PlaylistService playlistService;

    @Autowired
    PlaylistController playlistController;

    @Test
    public void whenNoPlaylistsExists_ThenReturnEmptyList(){
        when(playlistService.getAllPlaylists()).thenReturn(Collections.emptyList());

        ResponseEntity<List<PlaylistResponseModel>> response = playlistController.getAllPlaylists();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(response.getBody(), Collections.<PlaylistResponseModel>emptyList());
        verify(playlistService, times(1)).getAllPlaylists();
    }

    @Test
    public void whenPlaylistExists_ThenReturnPlaylist(){
        PlaylistResponseModel playlistResponseModel = PlaylistResponseModel.builder()
                .identifier("test1111")
                .name("test")
                .songs(List.of(SongResponseModel.builder().build()))
                .duration(Time.valueOf(LocalTime.of(2, 3, 1)))
                .build();

        when(playlistService.getAllPlaylists()).thenReturn(List.of(playlistResponseModel));

        ResponseEntity<List<PlaylistResponseModel>> response = playlistController.getAllPlaylists();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(List.of(playlistResponseModel), response.getBody());
        assertEquals(1, response.getBody().size());
        verify(playlistService, times(1)).getAllPlaylists();
    }

    @Test
    public void whenPlaylistExists_ThenReturnPlaylistById(){
        PlaylistResponseModel playlistResponseModel = PlaylistResponseModel.builder()
                .identifier("test1111")
                .name("test")
                .songs(List.of(SongResponseModel.builder().build()))
                .duration(Time.valueOf(LocalTime.of(2, 3, 1)))
                .build();

        when(playlistService.getPlaylistById("test1111")).thenReturn(playlistResponseModel);
        ResponseEntity<PlaylistResponseModel> response = playlistController.getPlaylistById("test1111");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playlistResponseModel, response.getBody());
        verify(playlistService, times(1)).getPlaylistById("test1111");
    }

    @Test
    public void whenPlaylistIdInvalidOnGet_ThenThrowNotFoundException(){
        when(playlistService.getPlaylistById("invalid")).thenThrow(new NotFoundException("Playlist with id invalid not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> playlistController.getPlaylistById("invalid"));

        assertEquals("Playlist with id invalid not found", exception.getMessage());
        verify(playlistService, times(1)).getPlaylistById("invalid");
    }

    @Test
    public void whenPlaylistValid_ThenReturnNewPlaylist(){
        PlaylistResponseModel playlistResponseModel = PlaylistResponseModel.builder()
                .name("test")
                .songs(List.of(SongResponseModel.builder().build()))
                .build();

        PlaylistRequestModel playlistRequestModel = PlaylistRequestModel.builder()
                .name("test")
                .songs(List.of("100e8400-e29b-41d4-a716-446655440011"))
                .build();

        when(playlistService.addPlaylist(playlistRequestModel)).thenReturn(playlistResponseModel);

        ResponseEntity<PlaylistResponseModel> response = playlistController.addPlaylist(playlistRequestModel);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playlistResponseModel, response.getBody());
        verify(playlistService, times(1)).addPlaylist(playlistRequestModel);
    }

    @Test
    public void whenPlaylistExists_ThenReturnUpdatedPlaylist(){
        PlaylistResponseModel playlistResponseModel = PlaylistResponseModel.builder()
                .identifier("test1111")
                .name("test")
                .songs(List.of(SongResponseModel.builder().build()))
                .build();

        PlaylistRequestModel playlistRequestModel = PlaylistRequestModel.builder()
                .name("test")
                .songs(List.of("100e8400-e29b-41d4-a716-446655440011"))
                .build();

        when(playlistService.updatePlaylist(playlistRequestModel, "test1111")).thenReturn(playlistResponseModel);

        ResponseEntity<PlaylistResponseModel> response = playlistController.updatePlaylist("test1111", playlistRequestModel);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playlistResponseModel, response.getBody());
        verify(playlistService, times(1)).updatePlaylist(playlistRequestModel, "test1111");
    }

    @Test
    public void whenPlaylistDoesNotExistOnUpdate_ThenThrowNotFoundException(){
        PlaylistRequestModel playlistRequestModel = PlaylistRequestModel.builder()
                .name("test")
                .songs(List.of("100e8400-e29b-41d4-a716-446655440011"))
                .build();

        when(playlistService.updatePlaylist(playlistRequestModel, "invalid")).thenThrow(new NotFoundException("Playlist with id invalid not found"));

        NotFoundException exception = assertThrows(NotFoundException.class, () -> playlistController.updatePlaylist("invalid", playlistRequestModel));

        assertEquals("Playlist with id invalid not found", exception.getMessage());
        verify(playlistService, times(1)).updatePlaylist(playlistRequestModel, "invalid");
    }

    @Test
    public void whenPlaylistExists_ThenDeletePlaylist(){
        doNothing().when(playlistService).deletePlaylist("test1111");

        ResponseEntity<Void> response = playlistController.deletePlaylist("test1111");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(playlistService, times(1)).deletePlaylist("test1111");
    }

    @Test
    public void whenPlaylistDoesNotExistOnDelete_ThenThrowNotFoundException(){
        doThrow(new NotFoundException("Playlist with id invalid not found")).when(playlistService).deletePlaylist("invalid");

        NotFoundException exception = assertThrows(NotFoundException.class, () -> playlistController.deletePlaylist("invalid"));

        assertEquals("Playlist with id invalid not found", exception.getMessage());
        verify(playlistService, times(1)).deletePlaylist("invalid");
    }

    @Test
    public void whenPlaylistExists_ThenReturnAllArtists(){
        List<ArtistResponseModel> artistResponseModels = new ArrayList<>();
        artistResponseModels.add(ArtistResponseModel.builder().identifier("100e8400-e29b-41d4-a716-446655440001").build());
        artistResponseModels.add(ArtistResponseModel.builder().identifier("100e8400-e29b-41d4-a716-446655440002").build());

        when(playlistService.getAllArtists("test1111")).thenReturn(artistResponseModels);

        ResponseEntity<List<ArtistResponseModel>> response = playlistController.getAllArtists("test1111");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(artistResponseModels, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(playlistService, times(1)).getAllArtists("test1111");
    }

    @Test
    public void whenPlaylistDoesNotExistOnGetAllArtists_ThenThrowNotFoundException(){
        when(playlistService.getAllArtists("invalid")).thenThrow(new NotFoundException("Playlist with id invalid not found"));
        NotFoundException exception = assertThrows(NotFoundException.class, () -> playlistController.getAllArtists("invalid"));
        assertEquals("Playlist with id invalid not found", exception.getMessage());
        verify(playlistService, times(1)).getAllArtists("invalid");
    }

    @Test
    public void whenUserExists_ThenReturnAllPlaylists(){
        List<PlaylistResponseModel> playlistResponseModels = new ArrayList<>();
        playlistResponseModels.add(PlaylistResponseModel.builder().identifier("100e8400-e29b-41d4-a716-446655440001").build());
        playlistResponseModels.add(PlaylistResponseModel.builder().identifier("100e8400-e29b-41d4-a716-446655440002").build());

        when(playlistService.getPlaylistsByUserId("user")).thenReturn(playlistResponseModels);
        ResponseEntity<List<PlaylistResponseModel>> response = playlistController.getPlaylistsByUserId("user");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playlistResponseModels, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(playlistService, times(1)).getPlaylistsByUserId("user");
    }

    @Test
    public void whenUserDoesNotExistOnGetPlaylistsByUserId_ThenThrowNotFoundException(){
        when(playlistService.getPlaylistsByUserId("invalid")).thenThrow(new NotFoundException("User with id invalid not found"));
        NotFoundException exception = assertThrows(NotFoundException.class, () -> playlistController.getPlaylistsByUserId("invalid"));
        assertEquals("User with id invalid not found", exception.getMessage());
        verify(playlistService, times(1)).getPlaylistsByUserId("invalid");
    }

    @Test
    public void whenUserExists_ThenDeletePlaylists(){
        doNothing().when(playlistService).deletePlaylistsByUserId("user");
        ResponseEntity<Void> response = playlistController.deletePlaylistsByUserId("user");
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(playlistService, times(1)).deletePlaylistsByUserId("user");
    }

    @Test
    public void whenUserDoesNotExistOnDeletePlaylistsByUserId_ThenThrowNotFoundException(){
        doThrow(new NotFoundException("User with id invalid not found")).when(playlistService).deletePlaylistsByUserId("invalid");
        NotFoundException exception = assertThrows(NotFoundException.class, () -> playlistController.deletePlaylistsByUserId("invalid"));
        assertEquals("User with id invalid not found", exception.getMessage());
        verify(playlistService, times(1)).deletePlaylistsByUserId("invalid");
    }

    @Test
    public void whenSongExists_ThenReturnPlaylists(){
        List<PlaylistResponseModel> playlistResponseModels = new ArrayList<>();
        playlistResponseModels.add(PlaylistResponseModel.builder().identifier("100e8400-e29b-41d4-a716-446655440001").build());
        playlistResponseModels.add(PlaylistResponseModel.builder().identifier("100e8400-e29b-41d4-a716-446655440002").build());

        when(playlistService.getPlaylistsBySongId("100e8400-e29b-41d4-a716-446655440001")).thenReturn(playlistResponseModels);
        ResponseEntity<List<PlaylistResponseModel>> response = playlistController.getPlaylistsBySongId("100e8400-e29b-41d4-a716-446655440001");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(playlistResponseModels, response.getBody());
        assertEquals(2, response.getBody().size());
        verify(playlistService, times(1)).getPlaylistsBySongId("100e8400-e29b-41d4-a716-446655440001");
    }

    @Test
    public void whenSongDoesNotExistOnGetPlaylistsBySongId_ThenThrowNotFoundException(){
        when(playlistService.getPlaylistsBySongId("invalid")).thenThrow(new NotFoundException("Song with id invalid not found"));
        NotFoundException exception = assertThrows(NotFoundException.class, () -> playlistController.getPlaylistsBySongId("invalid"));
        assertEquals("Song with id invalid not found", exception.getMessage());
        verify(playlistService, times(1)).getPlaylistsBySongId("invalid");
    }
}
