package com.github.rybalkin_an.app.wiremock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rybalkin_an.app.restassured.user.steps.UserApi;
import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.model.UserData;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static org.apache.hc.core5.http.HttpStatus.SC_OK;
import static org.apache.hc.core5.http.HttpStatus.SC_UNPROCESSABLE_ENTITY;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@WireMockTest(httpPort = 8081)
public class WiremockUserControllerTest {

    private final String path = "/todos/1";

    private User givenUser;

    private UserApi userApi;

    private final ObjectMapper mapper = new ObjectMapper();

    @LocalServerPort
    private int randomServerPort;

    @BeforeEach
    void setup(){
        givenUser = new TestUser();

        userApi = new UserApi(randomServerPort);
    }

    @Test
    public void givenUser_andExternalServiceReturn500_whenSaveUserData_then422() {
        WireMock.stubFor(WireMock.get(path).willReturn(serverError()));

        User createdUser = userApi.create(givenUser);

        userApi.assertSaveUserDataToUser(createdUser.getId())
                .assertThat()
                .statusCode(SC_UNPROCESSABLE_ENTITY)
                .and().body(equalTo("500 Internal Server Error from GET http://localhost:8081/todos/1"));
    }

    @Test
    public void givenUser_andExternalServiceReturnUserData_whenSaveUserData_thenDataIsSaved(TestInfo testInfo) throws JsonProcessingException {
        UserData userData = new UserData();
        userData.setId(1L);
        userData.setUserId(1L);
        userData.setCompleted(false);
        userData.setTitle(testInfo.getDisplayName());

        ResponseDefinitionBuilder responseWithUserData = aResponse()
                .withStatus(SC_OK)
                .withBody(mapper.writeValueAsString(userData))
                .withHeader("Content-Type", "application/json");

        WireMock.stubFor(WireMock.get(path).willReturn(responseWithUserData));

        User createdUser = userApi.create(givenUser);

        userApi.assertSaveUserDataToUser(createdUser.getId())
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("userData.userId", equalTo(1))
                .body("userData.id", equalTo(1))
                .body("userData.title", equalTo(testInfo.getDisplayName()))
                .body("userData.completed", equalTo(false));
    }
}