package com.champlain.songservice.presentationlayer;

import com.champlain.songservice.buisnesslogiclayer.SongService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/songs")
public class SongController {
    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping()
    public ResponseEntity<List<SongResponseModel>> getAllSongs() {
        return ResponseEntity.ok().body(songService.getAllSongs());
    }

    @GetMapping("/{songId}")
    public ResponseEntity<SongResponseModel> getSongById(@PathVariable String songId) {
        return ResponseEntity.ok().body(songService.getSongById(songId));
    }

    @PostMapping()
    public ResponseEntity<SongResponseModel> addSong(@RequestBody SongRequestModel song) {
        return ResponseEntity.status(HttpStatus.CREATED).body(songService.addSong(song));
    }

    @PutMapping("/{songId}")
    public ResponseEntity<SongResponseModel> updateSong(@PathVariable String songId, @RequestBody SongRequestModel song) {
        return ResponseEntity.ok().body(songService.updateSong(song, songId));
    }

    @DeleteMapping("/{songId}")
    public ResponseEntity<Void> deleteSong(@PathVariable String songId) {
        songService.deleteSong(songId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<SongResponseModel>> getSongsByArtistId(@PathVariable String artistId) {
        return ResponseEntity.ok().body(songService.getSongsByArtistId(artistId));
    }
}
