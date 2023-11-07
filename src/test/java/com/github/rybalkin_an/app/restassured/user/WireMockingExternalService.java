package com.github.rybalkin_an.app.restassured.user;

import com.github.rybalkin_an.app.restassured.user.steps.UserApi;
import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.model.User;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;

@SpringBootTest
@WireMockTest(httpPort = 8081)
public class WireMockingExternalService {

    private String path = "/todos/1";

    @Value("${http.clients.todos.url}")
    private String todosUrl;

    @Value("${http.clients.todos.port}")
    private int todosPort;

    private UserApi userApi;

    @LocalServerPort
    protected int randomServerPort;

    @BeforeEach
    void mock( ){
        userApi = new UserApi(randomServerPort);

        todosPort = 12345;
        todosUrl = "http://localhost/";
        stubFor(get(urlMatching("/todos/.*"))
                .willReturn(aResponse()
                        .withStatus(SC_NOT_FOUND)));
    }

    @Test
    void test_something1_with_wiremock() {
        given()
                .baseUri("http://localhost/")
                .port(8081)
                .when()
                .get(path) // Use the path for the GET request
                .then().log().all()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    void test_something_with_wiremock() {
        User givenUser = new TestUser();

        User createdUser = userApi.create(givenUser);

        userApi.saveUserDataToUser(createdUser.getId());
    }
}