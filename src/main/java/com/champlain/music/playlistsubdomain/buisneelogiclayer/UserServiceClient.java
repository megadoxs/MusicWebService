package com.champlain.music.playlistsubdomain.buisneelogiclayer;

import com.champlain.music.playlistsubdomain.presentationlayer.UserResponseModel;
import com.champlain.music.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Component
public class UserServiceClient {
    private final RestTemplate restTemplate;
    private final String CLIENT_SERVICE_BASE_URL;

    public UserServiceClient(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
        String UserServiceHost = "localhost";
        String UserServicePort = "8080";
        CLIENT_SERVICE_BASE_URL = "http://" + UserServiceHost + ":" + UserServicePort + "/api/v1/users";
    }

    public UserResponseModel getUserById(String userId) {
        try {
            String url = CLIENT_SERVICE_BASE_URL + "/" + userId;
            return restTemplate.getForObject(url, UserResponseModel.class);
        }
        catch (HttpClientErrorException ex) {
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
