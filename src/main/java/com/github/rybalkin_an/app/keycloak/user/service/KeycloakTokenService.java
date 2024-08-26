package com.github.rybalkin_an.app.keycloak.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
public class KeycloakTokenService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakTokenService.class);

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public String getBearerToken(String username, String password) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("username", username);
        body.add("password", password);

        logger.info("Sending request to Keycloak for user: {}", username);
        logger.debug("Request URL: {}", serverUrl + "/realms/" + realm + "/protocol/openid-connect/token");
        logger.debug("Request Headers: {}", headers);
        logger.debug("Request Body: {}", body);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                serverUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        logger.info("Received response from Keycloak for user: {}", username);
        logger.debug("Response Status Code: {}", response.getStatusCode());
        logger.debug("Response Body: {}", response.getBody());

        Map<String, String> responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("access_token")) {
            return responseBody.get("access_token");
        } else {
            logger.error("Failed to retrieve access token for user: {}", username);
            throw new RuntimeException("Failed to retrieve access token");
        }
    }
}
