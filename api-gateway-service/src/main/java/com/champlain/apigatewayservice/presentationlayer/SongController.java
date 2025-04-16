package com.champlain.apigatewayservice.presentationlayer;

import com.champlain.apigatewayservice.buisnesslogiclayer.SongService;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongRequestModel;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/songs")
public class SongController {
    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping()
    public ResponseEntity<List<SongResponseModel>> getSongs() {
        return ResponseEntity.ok().body(songService.getSongs());
    }

    @GetMapping("/{songId}")
    public ResponseEntity<SongResponseModel> getSongById(@PathVariable String songId) {
        return ResponseEntity.ok().body(songService.getSongById(songId));
    }

    @PostMapping()
    public ResponseEntity<SongResponseModel> addSong(@RequestBody SongRequestModel songRequestModel) {
        return ResponseEntity.ok().body(songService.addSong(songRequestModel));
    }

    @PutMapping("/{songId}")
    public ResponseEntity<SongResponseModel> updateSong(@RequestBody SongRequestModel songRequestModel, @PathVariable String songId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.updateSong(songRequestModel, songId));
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<SongResponseModel> deleteSong(@PathVariable String songId) {
        songService.deleteSong(songId);
        return ResponseEntity.noContent().build();
    }
}
