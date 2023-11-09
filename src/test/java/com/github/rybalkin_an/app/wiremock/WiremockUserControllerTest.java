package com.github.rybalkin_an.app.wiremock;

import com.github.rybalkin_an.app.restassured.user.steps.UserApi;
import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.model.User;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@WireMockTest(httpPort = 8081)
public class WiremockUserControllerTest {

    private UserApi userApi;

    @LocalServerPort
    protected int randomServerPort;

    @BeforeEach
    void setupUserApi(){
        userApi = new UserApi(randomServerPort);
    }

    @Test
    public void givenUser_andExternalServiceReturn500_whenSaveUserData_then422() {
        User givenUser = new TestUser();

        User createdUser = userApi.create(givenUser);

        userApi.assertSaveUserDataToUser(createdUser.getId())
                .assertThat()
                .statusCode(422)
                .and().body(equalTo("An exception was encountered from the external service"));
    }
}