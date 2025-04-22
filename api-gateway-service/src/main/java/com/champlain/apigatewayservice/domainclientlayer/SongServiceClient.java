package com.champlain.apigatewayservice.domainclientlayer;

import com.champlain.apigatewayservice.presentationlayer.songdto.SongRequestModel;
import com.champlain.apigatewayservice.presentationlayer.songdto.SongResponseModel;
import com.champlain.apigatewayservice.utils.exceptions.DuplicateSongTitleException;
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

import static org.springframework.http.HttpStatus.*;

@Component
@Slf4j
public class SongServiceClient {
    private final RestTemplate restTemplate;
    private final String SONG_SERVICE_BASE_URL;

    public SongServiceClient(RestTemplate restTemplate, @Value("${app.song-service.host}") String host, @Value("${app.song-service.port}") String port) {
        this.restTemplate = restTemplate;
        SONG_SERVICE_BASE_URL = "http://" + host + ":" + port + "/api/v1/songs";
    }

    public List<SongResponseModel> getSongs() {
        try {
            return List.of(restTemplate.getForObject(SONG_SERVICE_BASE_URL, SongResponseModel[].class));
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public SongResponseModel getSongById(String songId) {
        try {
            return restTemplate.getForObject(SONG_SERVICE_BASE_URL + "/" + songId, SongResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public SongResponseModel addSong(SongRequestModel songRequestModel) {
        try {
            return restTemplate.postForObject(SONG_SERVICE_BASE_URL, songRequestModel, SongResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public SongResponseModel updateSong(SongRequestModel songRequestModel, String songId) {
        try {
            return restTemplate.exchange(SONG_SERVICE_BASE_URL + "/" + songId, HttpMethod.PUT, new HttpEntity<>(songRequestModel, new HttpHeaders() {{
                setContentType(MediaType.APPLICATION_JSON);
            }}), SongResponseModel.class).getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void deleteSong(String songId) {
        try {
            restTemplate.delete(SONG_SERVICE_BASE_URL + "/" + songId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException e) {
        if (e.getStatusCode() == BAD_REQUEST)
            throw new DuplicateSongTitleException(e.getMessage());
        else if (e.getStatusCode() == NOT_FOUND)
            throw new NotFoundException(e.getMessage());
        return e;
    }
}
