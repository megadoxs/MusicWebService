package com.champlain.playlistservice.domainclientlayer;

import com.champlain.playlistservice.presentationlayer.UserResponseModel;
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
public class UserServiceClient {
    private final RestTemplate restTemplate;
    private final String CLIENT_SERVICE_BASE_URL;

    public UserServiceClient(RestTemplate restTemplate, @Value("${app.user-service.host}") String UserServiceHost, @Value("${app.user-service.port}") String UserServicePort) {
        this.restTemplate = restTemplate;
        CLIENT_SERVICE_BASE_URL = "http://" + UserServiceHost + ":" + UserServicePort + "/api/v1/users";
    }

    public UserResponseModel getUserById(String userId) {
        try {
            String url = CLIENT_SERVICE_BASE_URL + "/" + userId;
            return restTemplate.getForObject(url, UserResponseModel.class);
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
