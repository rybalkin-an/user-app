package com.github.rybalkin_an.app.restassured.user;

import com.github.rybalkin_an.app.restassured.BaseRequest;
import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.model.User;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;


public class UserTest extends BaseRequest {

    private final String path = "/users";

    @Test
    void givenUser_whenPostUser_thenUserCreated(){
        User givenUser = new TestUser();

        User createdUser = createUser(givenUser)
                .statusCode(SC_CREATED)
                .and()
                .extract().as(User.class);

        validateUser(createdUser, givenUser);
    }

    @Test
    void whenGetUsers_thenReturnEmptyList(){
        given(requestSpec)
                .when()
                .get(path)
                .then()
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("$", is(empty()));
    }

    @Test
    void givenCreatedUser_whenGetUsers_thenUserCreated(){
        User givenUser = new TestUser();

        createUser(givenUser);

        List<User> userList = getUsers();

        User createdUser = userList.get(0);

        validateUser(createdUser, givenUser);
    }

    private void validateUser(User createdUser, User givenUser){
        assertThat(createdUser.getId()).isOfAnyClassIn(UUID.class);
        assertThat(createdUser.getBirthdate()).isEqualTo(givenUser.getBirthdate());
        assertThat(createdUser.getFirstName()).isEqualTo(givenUser.getFirstName());
        assertThat(createdUser.getLastName()).isEqualTo(givenUser.getLastName());
        assertThat(createdUser.getRegistrationDate()).isEqualTo(givenUser.getRegistrationDate());
        assertThat(createdUser.getVersion()).isEqualTo(null);
    }

    private ValidatableResponse createUser(User givenUser){
        return given(requestSpec)
                .body(givenUser)
                .when()
                .post(path)
                .then();
    }

    private List<User> getUsers(){
        return given(requestSpec)
                .when()
                .get(path)
                .then()
                .extract().jsonPath().getList(".", User.class);
    }
}
