package com.champlain.userservice.domainclientlayer;

import com.champlain.userservice.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

    public void deletePlaylistsByUserId(String userId) {
        try {
            restTemplate.delete(PLAYLIST_SERVICE_BASE_URL + "/user/" + userId);
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
