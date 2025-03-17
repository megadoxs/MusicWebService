package com.champlain.music.playlistsubdomain.buisneelogiclayer;

import com.champlain.music.playlistsubdomain.presentationlayer.CustomerResponseModel;
import com.champlain.music.songsubdomain.presentationlayer.ArtistResponseModel;
import com.champlain.music.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Component
public class CustomerServiceClient { //TODO might change depending if chris calls it user or customer
    private final RestTemplate restTemplate;
    private final String CLIENT_SERVICE_BASE_URL;

    public CustomerServiceClient(RestTemplateBuilder restTemplate) {
        this.restTemplate = restTemplate.build();
        String customerServiceHost = "localhost";
        String customerServicePort = "8080";
        CLIENT_SERVICE_BASE_URL = "http://" + customerServiceHost + ":" + customerServicePort + "/api/v1/customers";
    }

    public CustomerResponseModel getCustomerById(String customerId) {
        try {
            String url = CLIENT_SERVICE_BASE_URL + "/" + customerId;
            return restTemplate.getForObject(url, CustomerResponseModel.class);
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
