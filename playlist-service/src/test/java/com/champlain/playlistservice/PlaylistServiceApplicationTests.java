package com.champlain.playlistservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest()
class PlaylistServiceApplicationTests {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Test
    void contextLoads() {
    }
}