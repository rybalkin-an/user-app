package com.github.rybalkin_an.app.wiremock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rybalkin_an.app.user.model.UserData;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.notFound;
import static com.github.tomakehurst.wiremock.client.WireMock.notMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@WireMockTest(httpPort = 8081)
public class WiremockExternalApiTest {

    @Value("${external.api.url}")
    private String host;

    private final String path = "todos/1";

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setupMocks() throws JsonProcessingException {
        String path = "/todos/1";
        stubFor(get(path)
                .withHeader("mock", equalTo("500"))
                .willReturn(serverError()));

        stubFor(get(path)
                .withHeader("mock", equalTo("404"))
                .willReturn(notFound()));

        UserData userData = new UserData();
        userData.setId(1L);
        userData.setUserId(1L);
        userData.setCompleted(false);
        userData.setTitle("WiremockExternalApiTest");

        ResponseDefinitionBuilder responseWithUserData = aResponse()
                .withStatus(SC_OK)
                .withBody(mapper.writeValueAsString(userData))
                .withHeader("Content-Type", "application/json");

        stubFor(get(path)
                .withHeader("mock", equalTo("200"))
                .willReturn(responseWithUserData));

        stubFor(get(path)
                .withHeader("mock", notMatching("200|404|500"))
                .willReturn(aResponse().proxiedFrom("https://jsonplaceholder.typicode.com")));
    }

    @ParameterizedTest(name = "Request with header 'mock' = {0} should return matching status code")
    @ValueSource(strings = {"500", "404", "200"})
    public void whenSendRequestWithHeader_thenReturnMatchingStatus(String headerValue) {
        RestAssured.given()
                .header("mock", headerValue)
                .get(host + path)
                .then()
                .assertThat()
                .statusCode(Integer.parseInt(headerValue));
    }

    @Test
    public void whenSendRequestWithoutHeader_getResponseProxiedFromExternalApi() {
        RestAssured.given()
                .get(host + path)
                .then()
                .assertThat()
                .statusCode(SC_OK);
    }
}