package com.github.rybalkin_an.app.restassured.user;

import com.github.rybalkin_an.app.user.client.ExternalApiClient;
import com.github.rybalkin_an.app.user.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.mockito.Mockito.when;

public class MockingExternalService {

    private ExternalApiClient client;

    @BeforeEach
    public void setUp() {
        client = Mockito.mock(ExternalApiClient.class);
    }

    @Test
    public void testSomethingWithMockedService() {
        // Setup the expected response
        when(client.getUserData()).thenThrow(new BusinessException("Not found"));

        // Use RestAssured to make your actual call, which is now intercepted by Mockito
        given()
                .baseUri("https://jsonplaceholder.typicode.com")
                .when()
                .get("/todo/1")
                .then()
                .statusCode(404);

        // Verify that the mock was called
        Mockito.verify(client).getUserData();
    }
}