package com.champlain.apigatewayservice.presentationlayer;

import com.champlain.apigatewayservice.buisnesslogiclayer.PlaylistService;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping()
    public ResponseEntity<List<PlaylistResponseModel>> getPlaylists() {
        return ResponseEntity.ok().body(playlistService.getPlaylists());
    }

    @GetMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponseModel> getPlaylistById(@PathVariable String playlistId) {
        return ResponseEntity.ok().body(playlistService.getPlaylistById(playlistId));
    }

    @PostMapping()
    public ResponseEntity<PlaylistResponseModel> addPlaylist(@RequestBody PlaylistRequestModel playlistRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playlistService.addPlaylist(playlistRequestModel));
    }

    @PutMapping("/{playlistId}")
    public ResponseEntity<PlaylistResponseModel> updatePlaylist(@RequestBody PlaylistRequestModel playlistRequestModel, @PathVariable String playlistId) {
        return ResponseEntity.ok().body(playlistService.updatePlaylist(playlistRequestModel, playlistId));
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }
}
