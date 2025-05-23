package com.champlain.songservice.domainclientlayer;

import com.champlain.songservice.presentationlayer.ArtistResponseModel;
import com.champlain.songservice.utils.exceptions.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Component
public class ArtistServiceClient {
    private final RestTemplate restTemplate;
    private final String CLIENT_SERVICE_BASE_URL;

    public ArtistServiceClient(RestTemplate restTemplate, @Value("${app.artist-service.host}") String artistServiceHost, @Value("${app.artist-service.port}") String artistsServicePort) {
        this.restTemplate = restTemplate;
        CLIENT_SERVICE_BASE_URL = "http://" + artistServiceHost + ":" + artistsServicePort + "/api/v1/artists";
    }

    public ArtistResponseModel getArtistById(String ArtistId) {
        try {
            String url = CLIENT_SERVICE_BASE_URL + "/" + ArtistId;
            return restTemplate.getForObject(url, ArtistResponseModel.class);
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
