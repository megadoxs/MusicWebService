package com.champlain.apigatewayservice.presentationlayer;

import com.champlain.apigatewayservice.buisnesslogiclayer.ArtistService;
import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistResponseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/artists")
public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @GetMapping()
    public ResponseEntity<List<ArtistResponseModel>> getArtists() {
        return ResponseEntity.ok().body(artistService.getArtists());
    }

    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistResponseModel> getArtistById(@PathVariable String artistId) {
        return ResponseEntity.ok().body(artistService.getArtistById(artistId));
    }

    @PostMapping
    public ResponseEntity<ArtistResponseModel> addArtist(@RequestBody ArtistRequestModel artistRequestModel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(artistService.addArtist(artistRequestModel));
    }

    @PutMapping("/{artistId}")
    public ResponseEntity<ArtistResponseModel> updateArtist(@RequestBody ArtistRequestModel artistRequestModel, @PathVariable String artistId) {
        return ResponseEntity.ok().body(artistService.updateArtist(artistRequestModel, artistId));
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> deleteArtist(@PathVariable String artistId) {
        artistService.deleteArtist(artistId);
        return ResponseEntity.noContent().build();
    }
}
