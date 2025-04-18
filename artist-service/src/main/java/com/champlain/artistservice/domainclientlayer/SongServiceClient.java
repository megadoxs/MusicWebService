package com.champlain.artistservice.domainclientlayer;

import com.champlain.artistservice.presentationlayer.ArtistResponseModel;
import com.champlain.artistservice.presentationlayer.SongRequestModel;
import com.champlain.artistservice.presentationlayer.SongResponseModel;
import com.champlain.artistservice.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Component
public class SongServiceClient {
    private final RestTemplate restTemplate;
    private final String SONG_SERVICE_BASE_URL;

    public SongServiceClient(RestTemplateBuilder restTemplate, @Value("${app.song-service.host}") String host, @Value("${app.song-service.port}") String port) {
        this.restTemplate = restTemplate.build();
        SONG_SERVICE_BASE_URL = "http://" + host + ":" + port + "/api/v1/songs";
    }

    public void deleteSongById(String songId) {
        try {
            restTemplate.delete(SONG_SERVICE_BASE_URL + "/" + songId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public List<SongResponseModel> getSongsByArtistId(String artistId) {
        try {
            return List.of(restTemplate.getForObject(SONG_SERVICE_BASE_URL + "/artist/" + artistId, SongResponseModel[].class));
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void removeArtistFromSong(SongResponseModel song, String artistId) {
        SongRequestModel songRequestModel = new SongRequestModel();
        songRequestModel.setArtists(song.getArtists().stream().map(ArtistResponseModel::getIdentifier).filter(id -> !id.equals(artistId)).collect(Collectors.toList()));
        songRequestModel.setTitle(song.getTitle());
        songRequestModel.setGenre(song.getGenre());
        songRequestModel.setReleaseDate(song.getReleaseDate());
        songRequestModel.setDuration(song.getDuration());

        try {
            restTemplate.put(SONG_SERVICE_BASE_URL + "/" + song.getIdentifier(), songRequestModel);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }


    private RuntimeException handleHttpClientException(HttpClientErrorException ex) {
        if (ex.getStatusCode() == NOT_FOUND) {
            return new NotFoundException(ex.getMessage());
        }
        log.warn("Got a unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
        log.warn("Error body: {}", ex.getResponseBodyAsString());
        return ex;
    }
}
