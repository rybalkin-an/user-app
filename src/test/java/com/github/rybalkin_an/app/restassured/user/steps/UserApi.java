package com.github.rybalkin_an.app.restassured.user.steps;

import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.service.UserService;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.*;

public class UserApi implements UserService {

    private final String path = "/users";
    private final String url = "http://localhost/api";

    private final RequestSpecification requestSpec;

    public UserApi(int port) {
        this.requestSpec = new RequestSpecBuilder()
                .setPort(port)
                .setContentType(JSON)
                .setAccept(JSON)
                .setBaseUri(url)
                .build();
    }

    @Override
    public List<User> findAll() {
        return given(requestSpec)
                .when().get(path)
                .then().statusCode(SC_OK)
                .and().extract().jsonPath().getList(".", User.class);
    }

    @Override
    public User findById(UUID uuid) {
        return given(requestSpec)
                .when().get(path + "/" + uuid.toString())
                .then().statusCode(SC_OK)
                .and().extract().as(User.class);
    }

    @Override
    public User create(User entity) {
        return given(requestSpec).body(entity)
                .when().post(path)
                .then().statusCode(SC_CREATED)
                .and().extract().as(User.class);
    }

    @Override
    public User update(UUID uuid, User entity) {
        return given(requestSpec).body(entity)
                .when().post(path + "/" + uuid.toString())
                .then().statusCode(SC_OK)
                .and().extract().as(User.class);
    }

    @Override
    public void delete(UUID uuid) {
        given(requestSpec)
                .when().delete(path + "/" + uuid.toString())
                .then().statusCode(SC_NO_CONTENT);
    }

    @Override
    public User saveUserDataToUser(UUID id) {
        return given(requestSpec)
                .when().patch(path + "/" + id.toString())
                .then().statusCode(SC_OK)
                .and().extract().as(User.class);
    }

    public ValidatableResponse assertSaveUserDataToUser(UUID id) {
        return given(requestSpec)
                .when().patch(path + "/" + id.toString())
                .then();
    }
}
