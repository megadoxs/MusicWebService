package com.champlain.playlistservice.domainclientlayer;

import com.champlain.playlistservice.presentationlayer.SongResponseModel;
import com.champlain.playlistservice.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Component
public class SongServiceClient {
    private final RestTemplate restTemplate;
    private final String CLIENT_SERVICE_BASE_URL;

    public SongServiceClient(RestTemplate restTemplate, @Value("${app.song-service.host}") String songServiceHost, @Value("${app.song-service.port}") String songServicePort) {
        this.restTemplate = restTemplate;
        CLIENT_SERVICE_BASE_URL = "http://" + songServiceHost + ":" + songServicePort + "/api/v1/songs";
        log.debug(CLIENT_SERVICE_BASE_URL);
    }

    public SongResponseModel getSongById(String songId) {
        try {
            String url = CLIENT_SERVICE_BASE_URL + "/" + songId;
            return restTemplate.getForObject(url, SongResponseModel.class);
        } catch (HttpClientErrorException ex) {
            throw handleHttpClientException(ex);
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
