package com.champlain.playlistservice.businesslayer;

import com.champlain.playlistservice.buisneelogiclayer.PlaylistService;
import com.champlain.playlistservice.dataaccesslayer.playlist.Playlist;
import com.champlain.playlistservice.dataaccesslayer.playlist.PlaylistIdentifier;
import com.champlain.playlistservice.dataaccesslayer.playlist.PlaylistRepository;
import com.champlain.playlistservice.dataaccesslayer.song.Genre;
import com.champlain.playlistservice.datamapperlayer.PlaylistResponseModelMapper;
import com.champlain.playlistservice.domainclientlayer.SongServiceClient;
import com.champlain.playlistservice.domainclientlayer.UserServiceClient;
import com.champlain.playlistservice.presentationlayer.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("h2")
public class PlaylistServiceImplUnitTest {
    @Autowired
    PlaylistService playlistService;

    @MockitoBean
    SongServiceClient songServiceClient;

    @MockitoBean
    UserServiceClient userServiceClient;

    @MockitoBean
    PlaylistRepository playlistRepository;

    @MockitoSpyBean
    PlaylistResponseModelMapper playlistResponseModelMapper;

    @Test
    public void whenValid_SongId_ArtistId_UserId_thenProcessCustomerPurchaseOrder_ShouldSucceed () {
        PlaylistRequestModel playlistRequestModel = PlaylistRequestModel.builder()
                .name("Death Row Records")
                .songs(List.of("100e8400-e29b-41d4-a716-446655440011", "100e8400-e29b-41d4-a716-446655440016"))
                .user("c27242a2-abb9-45b2-a85d-ed9ffa15f92c")
                .build();

        ArtistResponseModel artistResponseModel1 = ArtistResponseModel.builder()
                .identifier("550e8400-e29b-41d4-a716-446655440001")
                .firstName("Marshall")
                .lastName("Mathers")
                .stageName("Eminem")
                .build();

        ArtistResponseModel artistResponseModel2 = ArtistResponseModel.builder()
                .identifier("550e8400-e29b-41d4-a716-446655440006")
                .firstName("Calvin")
                .lastName("Broadus")
                .stageName("Snoop Dogg")
                .build();

        SongResponseModel songResponseModel1 = SongResponseModel.builder()
                .identifier("100e8400-e29b-41d4-a716-446655440011")
                .title("Lose Yourself")
                .genre(Genre.HIP_HOP)
                .releaseDate(LocalDate.of(2002, 10, 28))
                .duration(Time.valueOf(LocalTime.of(0, 5, 26)))
                .artists(List.of(artistResponseModel1))
                .build();

        SongResponseModel songResponseModel2 = SongResponseModel.builder()
                .identifier("100e8400-e29b-41d4-a716-446655440016")
                .title("Drop It Like It's Hot")
                .genre(Genre.HIP_HOP)
                .releaseDate(LocalDate.of(2013, 8, 10))
                .duration(Time.valueOf(LocalTime.of(0, 3, 42)))
                .artists(List.of(artistResponseModel2))
                .build();

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId("c27242a2-abb9-45b2-a85d-ed9ffa15f92c")
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@example.com")
                .username("alicej95")
                .build();

        Playlist playlist = Playlist.builder()
                .id(1000)
                .identifier(new PlaylistIdentifier())
                .name("Death Row Records")
                .songs(List.of("100e8400-e29b-41d4-a716-446655440011", "100e8400-e29b-41d4-a716-446655440016"))
                .user("c27242a2-abb9-45b2-a85d-ed9ffa15f92c")
                .duration(Time.valueOf(LocalTime.of(0, 9, 8)))
                .build();

        when(songServiceClient.getSongById("100e8400-e29b-41d4-a716-446655440011")).thenReturn(songResponseModel1);
        when(songServiceClient.getSongById("100e8400-e29b-41d4-a716-446655440016")).thenReturn(songResponseModel2);
        when(userServiceClient.getUserById("c27242a2-abb9-45b2-a85d-ed9ffa15f92c")).thenReturn(userResponseModel);
        when(playlistRepository.save(any(Playlist.class))).thenReturn(playlist);

        PlaylistResponseModel playlistResponseModel = playlistService.addPlaylist(playlistRequestModel);


        assertNotNull(playlistResponseModel);
        assertNotNull(playlistResponseModel.getIdentifier());
        assertNotNull(playlistResponseModel.getUser());
        assertNotNull(playlistResponseModel.getSongs());
        assertEquals(playlistRequestModel.getName(), playlistResponseModel.getName());
        assertEquals(playlistRequestModel.getUser(), playlistResponseModel.getUser().getUserId());
        assertEquals(playlistRequestModel.getSongs().size(), playlistResponseModel.getSongs().size());
        assertEquals(playlistRequestModel.getSongs(), playlistResponseModel.getSongs().stream().map(SongResponseModel::getIdentifier).toList());

        assertEquals(artistResponseModel1.getIdentifier(), playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getIdentifier());
        assertEquals(artistResponseModel1.getFirstName(), playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getFirstName());
        assertEquals(artistResponseModel1.getLastName(), playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getLastName());
        assertEquals(artistResponseModel1.getStageName(), playlistResponseModel.getSongs().getFirst().getArtists().getFirst().getStageName());
        assertEquals(artistResponseModel2.getIdentifier(), playlistResponseModel.getSongs().getLast().getArtists().getFirst().getIdentifier());
        assertEquals(artistResponseModel2.getFirstName(), playlistResponseModel.getSongs().getLast().getArtists().getFirst().getFirstName());
        assertEquals(artistResponseModel2.getLastName(), playlistResponseModel.getSongs().getLast().getArtists().getFirst().getLastName());
        assertEquals(artistResponseModel2.getStageName(), playlistResponseModel.getSongs().getLast().getArtists().getFirst().getStageName());

        assertEquals(userResponseModel.getUserId(), playlistResponseModel.getUser().getUserId());
        assertEquals(userResponseModel.getFirstName(), playlistResponseModel.getUser().getFirstName());
        assertEquals(userResponseModel.getLastName(), playlistResponseModel.getUser().getLastName());
        assertEquals(userResponseModel.getUsername(), playlistResponseModel.getUser().getUsername());
        assertEquals(userResponseModel.getEmail(), playlistResponseModel.getUser().getEmail());

        assertEquals(songResponseModel1.getIdentifier(), playlistResponseModel.getSongs().getFirst().getIdentifier());
        assertEquals(songResponseModel1.getTitle(), playlistResponseModel.getSongs().getFirst().getTitle());
        assertEquals(songResponseModel1.getGenre(), playlistResponseModel.getSongs().getFirst().getGenre());
        assertEquals(songResponseModel1.getReleaseDate(), playlistResponseModel.getSongs().getFirst().getReleaseDate());
        assertEquals(songResponseModel1.getDuration(), playlistResponseModel.getSongs().getFirst().getDuration());
        assertEquals(songResponseModel1.getArtists(), playlistResponseModel.getSongs().getFirst().getArtists());
        assertEquals(songResponseModel2.getIdentifier(), playlistResponseModel.getSongs().getLast().getIdentifier());
        assertEquals(songResponseModel2.getTitle(), playlistResponseModel.getSongs().getLast().getTitle());
        assertEquals(songResponseModel2.getGenre(), playlistResponseModel.getSongs().getLast().getGenre());
        assertEquals(songResponseModel2.getReleaseDate(), playlistResponseModel.getSongs().getLast().getReleaseDate());
        assertEquals(songResponseModel2.getDuration(), playlistResponseModel.getSongs().getLast().getDuration());
        assertEquals(songResponseModel2.getArtists(), playlistResponseModel.getSongs().getLast().getArtists());

        verify(playlistResponseModelMapper, times(1)).entityToResponseModel(playlist);
    }

    @Test
    public void whenInvalid_SongId_thenAddPlaylist_ShouldThrowException() {
        PlaylistRequestModel playlistRequestModel = PlaylistRequestModel.builder()
                .name("Invalid Playlist")
                .songs(List.of("invalid-song-id"))
                .user("c27242a2-abb9-45b2-a85d-ed9ffa15f92c")
                .build();

        UserResponseModel userResponseModel = UserResponseModel.builder()
                .userId("c27242a2-abb9-45b2-a85d-ed9ffa15f92c")
                .firstName("Alice")
                .lastName("Johnson")
                .email("alice.johnson@example.com")
                .username("alicej95")
                .build();

        // Mock song service to throw exception for invalid song ID
        when(songServiceClient.getSongById("invalid-song-id"))
                .thenThrow(new RuntimeException("Song not found"));

        // Mock user service to still return a valid user
        when(userServiceClient.getUserById("c27242a2-abb9-45b2-a85d-ed9ffa15f92c"))
                .thenReturn(userResponseModel);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            playlistService.addPlaylist(playlistRequestModel);
        });

        assertEquals("Song not found", exception.getMessage());

        // Ensure no playlist is saved when exception occurs
        verify(playlistRepository, never()).save(any(Playlist.class));
        verify(playlistResponseModelMapper, never()).entityToResponseModel(any());
    }
}
