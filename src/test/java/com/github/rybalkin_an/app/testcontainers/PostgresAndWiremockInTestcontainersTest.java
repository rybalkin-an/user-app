package com.github.rybalkin_an.app.testcontainers;

import com.github.rybalkin_an.app.restassured.user.steps.UserApi;
import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.model.UserData;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ContainersConfig.class)
public class PostgresAndWiremockInTestcontainersTest {
//     API integration testing with WireMock and Testcontainers
//
//     postgres is running in a testcontainer
//     wiremock is running in a testcontainer
//     spring context is up
//     endpoint call within RestAssured


    @LocalServerPort
    private int randomServerPort;

    @Value("${external.api.url}")
    private String apiUrl;

    private UserApi userApi;

    @BeforeEach
    public void setUp() {
        userApi = new UserApi(randomServerPort);
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void testUpdateUser() {
        User givenUser = new TestUser();
        User createdUser = this.userApi.create(givenUser);
        this.userApi.saveUserDataToUser(createdUser.getId());
        User userWithUserData = this.userApi.findById(createdUser.getId());

        assertThat(userWithUserData.getUserData().getUserId()).isEqualTo(1);
        assertThat(userWithUserData.getUserData().getUserId()).isEqualTo(1);
        assertThat(userWithUserData.getUserData().getCompleted()).isEqualTo(false);
        assertThat(userWithUserData.getUserData().getTitle()).isEqualTo("Wiremock");
    }

    @Test
    void givenMockExternalApiInCotainer_whenExternalApi_thenReturnResponse() {
        UserData userData = RestAssured.given().baseUri(apiUrl)
                .when().get("/todos/1")
                .then().statusCode(SC_OK).extract().as(UserData.class);

        assertThat(userData.getUserId()).isEqualTo(1);
        assertThat(userData.getUserId()).isEqualTo(1);
        assertThat(userData.getCompleted()).isEqualTo(false);
        assertThat(userData.getTitle()).isEqualTo("Wiremock");
    }

}