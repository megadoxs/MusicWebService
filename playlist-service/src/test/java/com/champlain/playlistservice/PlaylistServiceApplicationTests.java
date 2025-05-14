package com.champlain.playlistservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

@SpringBootTest()
@ActiveProfiles("h2")
class PlaylistServiceApplicationTests {

    @Test
    void contextLoads() {
    }


//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        public RestTemplate restTemplate() {
//            return new RestTemplate();
//        }
//    }
}