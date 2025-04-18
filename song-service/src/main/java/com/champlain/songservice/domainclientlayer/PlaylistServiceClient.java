package com.champlain.songservice.domainclientlayer;

import com.champlain.songservice.presentationlayer.PlaylistRequestModel;
import com.champlain.songservice.presentationlayer.PlaylistResponseModel;
import com.champlain.songservice.presentationlayer.SongResponseModel;
import com.champlain.songservice.utils.exceptions.NotFoundException;
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
public class PlaylistServiceClient {
    private final RestTemplate restTemplate;
    private final String PLAYLIST_SERVICE_BASE_URL;

    public PlaylistServiceClient(RestTemplateBuilder restTemplate, @Value("${app.playlist-service.host}") String host, @Value("${app.playlist-service.port}") String port) {
        this.restTemplate = restTemplate.build();
        PLAYLIST_SERVICE_BASE_URL = "http://" + host + ":" + port + "/api/v1/playlists";
    }

    public void deletePlaylistById(String playlistId) {
        try {
            restTemplate.delete(PLAYLIST_SERVICE_BASE_URL + "/" + playlistId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public List<PlaylistResponseModel> getPlaylistsBySongId(String songId) {
        try {
            return List.of(restTemplate.getForObject(PLAYLIST_SERVICE_BASE_URL + "/song/" + songId, PlaylistResponseModel[].class));
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void removeSongFromPlaylist(PlaylistResponseModel playlist, String songId) {
        PlaylistRequestModel playlistRequestModel = new PlaylistRequestModel();
        playlistRequestModel.setName(playlist.getName());
        playlistRequestModel.setSongs(playlist.getSongs().stream().map(SongResponseModel::getIdentifier).filter(id -> !id.equals(songId)).collect(Collectors.toList()));
        playlistRequestModel.setUser(playlist.getUser());

        try {
            restTemplate.put(PLAYLIST_SERVICE_BASE_URL + "/" + playlist.getIdentifier(), playlistRequestModel);
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
