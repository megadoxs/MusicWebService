package com.champlain.playlistservice.presentationlayer;

import com.champlain.playlistservice.buisneelogiclayer.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping()
    public ResponseEntity<List<PlaylistResponseModel>> getAllPlaylists() {
        return ResponseEntity.ok().body(playlistService.getAllPlaylists());
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponseModel> getPlaylistById(@PathVariable String playlistId) {
        return ResponseEntity.ok().body(playlistService.getPlaylistById(playlistId));
    }

    @PostMapping()
    public ResponseEntity<PlaylistResponseModel> addPlaylist(@RequestBody PlaylistRequestModel playlist) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playlistService.addPlaylist(playlist));
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponseModel> updatePlaylist(@PathVariable String playlistId, @RequestBody PlaylistRequestModel playlist) {
        return ResponseEntity.ok().body(playlistService.updatePlaylist(playlist, playlistId));
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{playlistId}/artists")
    public ResponseEntity<List<ArtistResponseModel>> getAllArtists(@PathVariable String playlistId) {
        return ResponseEntity.ok().body(playlistService.getAllArtists(playlistId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PlaylistResponseModel>> getPlaylistsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok().body(playlistService.getPlaylistsByUserId(userId));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deletePlaylistsByUserId(@PathVariable String userId) {
        playlistService.deletePlaylistsByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/song/{songId}")
    public ResponseEntity<List<PlaylistResponseModel>> getPlaylistsBySongId(@PathVariable String songId) {
        return ResponseEntity.ok().body(playlistService.getPlaylistsBySongId(songId));
    }
}
