package com.champlain.artistservice.presentationlayer;

import com.champlain.artistservice.buisnesslogiclayer.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/artists")
public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping()
    public ResponseEntity<List<ArtistResponseModel>> getAllArtists() {
        return ResponseEntity.ok().body(artistService.getAllArtists());
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistResponseModel> getArtistById(@PathVariable String artistId) {
        return ResponseEntity.ok().body(artistService.getArtistById(artistId));
    }

    @PostMapping()
    public ResponseEntity<ArtistResponseModel> addArtist(@RequestBody ArtistRequestModel artist) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.addArtist(artist));
    }

    @PutMapping("/{artistId}")
    public ResponseEntity<ArtistResponseModel> updateArtist(@PathVariable String artistId, @RequestBody ArtistRequestModel artist) {
        return ResponseEntity.ok().body(artistService.updateArtist(artist, artistId));
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> deleteArtist(@PathVariable String artistId) {
        artistService.deleteArtist(artistId);
        return ResponseEntity.noContent().build();
    }
}
