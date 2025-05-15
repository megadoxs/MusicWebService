package com.champlain.apigatewayservice.domainclientlayer;

import com.champlain.apigatewayservice.presentationlayer.userdto.UserRequestModel;
import com.champlain.apigatewayservice.presentationlayer.userdto.UserResponseModel;
import com.champlain.apigatewayservice.utils.exceptions.DuplicateUsernameException;
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
public class UserServiceClient {
    private final RestTemplate restTemplate;
    private final String USER_SERVICE_BASE_URL;

    public UserServiceClient(RestTemplate restTemplate, @Value("${app.user-service.host}") String host, @Value("${app.user-service.port}") String port) {
        this.restTemplate = restTemplate;
        USER_SERVICE_BASE_URL = "http://" + host + ":" + port + "/api/v1/users";
    }

    public List<UserResponseModel> getUsers() {
        try {
            return List.of(restTemplate.getForObject(USER_SERVICE_BASE_URL, UserResponseModel[].class));
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public UserResponseModel getUserById(String userId) {
        try {
            return restTemplate.getForObject(USER_SERVICE_BASE_URL + "/" + userId, UserResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public UserResponseModel addUser(UserRequestModel userRequestModel) {
        try {
            return restTemplate.postForObject(USER_SERVICE_BASE_URL, userRequestModel, UserResponseModel.class);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public UserResponseModel updateUser(UserRequestModel userRequestModel, String userId) {
        try {
            return restTemplate.exchange(USER_SERVICE_BASE_URL + "/" + userId, HttpMethod.PUT, new HttpEntity<>(userRequestModel, new HttpHeaders() {{
                setContentType(MediaType.APPLICATION_JSON);
            }}), UserResponseModel.class).getBody();
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    public void deleteUser(String userId) {
        try {
            restTemplate.delete(USER_SERVICE_BASE_URL + "/" + userId);
        } catch (HttpClientErrorException e) {
            throw handleHttpClientException(e);
        }
    }

    private RuntimeException handleHttpClientException(HttpClientErrorException e) {
        if (e.getStatusCode() == BAD_REQUEST)
            throw new DuplicateUsernameException(e.getMessage());
        else if (e.getStatusCode() == NOT_FOUND)
            throw new NotFoundException(e.getMessage());
        return e;
    }
}
