package com.champlain.apigatewayservice.domainclientlayer;

import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistRequestModel;
import com.champlain.apigatewayservice.presentationlayer.artistdto.ArtistResponseModel;
import com.champlain.apigatewayservice.utils.exceptions.DuplicateStageNameException;
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
public class ArtistServiceClient {
    private final RestTemplate restTemplate;
    private final String ARTIST_SERVICE_BASE_URL;

    public ArtistServiceClient(RestTemplate restTemplate, @Value("${app.artist-service.host}") String host, @Value("${app.artist-service.port}") String port) {
        this.restTemplate = restTemplate;
        ARTIST_SERVICE_BASE_URL = "http://" + host + ":" + port + "/api/v1/artists";
    }

    public List<ArtistResponseModel> getArtists() {
        try {
            return List.of(restTemplate.getForObject(ARTIST_SERVICE_BASE_URL, ArtistResponseModel[].class));
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public ArtistResponseModel getArtistById(String artistId) {
        try {
            return restTemplate.getForObject(ARTIST_SERVICE_BASE_URL + "/" + artistId, ArtistResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public ArtistResponseModel addArtist(ArtistRequestModel artistRequestModel) {
        try {
            return restTemplate.postForObject(ARTIST_SERVICE_BASE_URL, artistRequestModel, ArtistResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public ArtistResponseModel updateArtist(ArtistRequestModel artistRequestModel, String artistId) {
        try {
            return restTemplate.exchange(ARTIST_SERVICE_BASE_URL + "/" + artistId, HttpMethod.PUT, new HttpEntity<>(artistRequestModel, new HttpHeaders() {{
                setContentType(MediaType.APPLICATION_JSON);
            }}), ArtistResponseModel.class).getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void deleteArtist(String artistId) {
        try {
            restTemplate.delete(ARTIST_SERVICE_BASE_URL + "/" + artistId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException e) {
        if (e.getStatusCode() == BAD_REQUEST)
            throw new DuplicateStageNameException(e.getMessage());
        else if (e.getStatusCode() == NOT_FOUND)
            throw new NotFoundException(e.getMessage());
        return e;
    }
}
