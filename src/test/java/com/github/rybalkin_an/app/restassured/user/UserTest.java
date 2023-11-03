package com.github.rybalkin_an.app.restassured.user;

import com.github.rybalkin_an.app.restassured.BaseRequest;
import com.github.rybalkin_an.app.restassured.user.steps.UserApi;
import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


public class UserTest extends BaseRequest {

    private final UserApi userApi = new UserApi();

    @Test
    void givenUser_whenPostUser_thenUserCreated(){
        User givenUser = new TestUser();

        User createdUser = userApi.create(givenUser);

        validateUser(createdUser, givenUser);
    }

    @Test
    void whenGetUsers_thenReturnEmptyList(){
        List<User> userList = userApi.findAll();

        assertThat(userList).isEmpty();
    }

    @Test
    void givenCreatedUser_whenGetUsers_thenUserCreated(){
        User givenUser = new TestUser();

        userApi.create(givenUser);

        List<User> userList = userApi.findAll();

        User createdUser = userList.get(0);

        validateUser(createdUser, givenUser);
    }

    @Test
    void givenCreatedUser_whenDeleteUser_then204(){
        User givenUser = new TestUser();

        userApi.create(givenUser);

        List<User> userList = userApi.findAll();
        UUID id = userList.get(0).getId();

        User createdUser = userApi.findById(id);
        validateUser(createdUser, givenUser);

        userApi.delete(id);
    }

    private void validateUser(User createdUser, User givenUser){
        assertThat(createdUser.getId()).isOfAnyClassIn(UUID.class);
        assertThat(createdUser.getBirthdate()).isEqualTo(givenUser.getBirthdate());
        assertThat(createdUser.getFirstName()).isEqualTo(givenUser.getFirstName());
        assertThat(createdUser.getLastName()).isEqualTo(givenUser.getLastName());
        assertThat(createdUser.getRegistrationDate()).isEqualTo(givenUser.getRegistrationDate());
        assertThat(createdUser.getVersion()).isEqualTo(null);
    }

}
