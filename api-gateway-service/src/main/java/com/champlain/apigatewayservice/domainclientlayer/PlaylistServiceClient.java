package com.champlain.apigatewayservice.domainclientlayer;

import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.playlistdto.PlaylistResponseModel;
import com.champlain.apigatewayservice.utils.exceptions.InvalidInputException;
import com.champlain.apigatewayservice.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Component
@Slf4j
public class PlaylistServiceClient {
    private final RestTemplate restTemplate;
    private final String PLAYLIST_SERVICE_BASE_URL;

    public PlaylistServiceClient(RestTemplate restTemplate, @Value("${app.playlist-service.host}") String host, @Value("${app.playlist-service.port}") String port) {
        this.restTemplate = restTemplate;
        PLAYLIST_SERVICE_BASE_URL = "http://" + host + ":" + port + "/api/v1/playlists";
    }

    public List<PlaylistResponseModel> getPlaylists() {
        try {
            return List.of(restTemplate.getForObject(PLAYLIST_SERVICE_BASE_URL, PlaylistResponseModel[].class));
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public PlaylistResponseModel getPlaylistById(String playlistId) {
        try {
            return restTemplate.getForObject(PLAYLIST_SERVICE_BASE_URL + "/" + playlistId, PlaylistResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public PlaylistResponseModel addPlaylist(PlaylistRequestModel playlistRequestModel) {
        try {
            return restTemplate.postForObject(PLAYLIST_SERVICE_BASE_URL, playlistRequestModel, PlaylistResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public PlaylistResponseModel updatePlaylist(PlaylistRequestModel playlistRequestModel, String playlistId) {
        try {
            return restTemplate.exchange(PLAYLIST_SERVICE_BASE_URL + "/" + playlistId, HttpMethod.PUT, new HttpEntity<>(playlistRequestModel, new HttpHeaders() {{
                setContentType(MediaType.APPLICATION_JSON);
            }}), PlaylistResponseModel.class).getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void deletePlaylist(String playlistId) {
        try {
            restTemplate.delete(PLAYLIST_SERVICE_BASE_URL + "/" + playlistId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException e) {
        if (e.getStatusCode() == UNPROCESSABLE_ENTITY)
            throw new InvalidInputException();
        else if (e.getStatusCode() == NOT_FOUND)
            throw new NotFoundException();
        return e;
    }
}
