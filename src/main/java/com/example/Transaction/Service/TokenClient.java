package com.example.Transaction.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.user-service.url}") // e.g. http://user-service:8080
    private String userServiceUrl;

    public String getEmailFromToken(String token) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    userServiceUrl + "/auth/decode",
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            return response.getBody(); // Email
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }


    public boolean isTokenValid(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> entity = new HttpEntity<>(headers);


        ResponseEntity<Boolean> response = restTemplate.exchange(
                userServiceUrl + "/auth/isValid",
                HttpMethod.GET,
                entity,
                Boolean.class
        );

        return Boolean.TRUE.equals(response.getBody());
    }
}
