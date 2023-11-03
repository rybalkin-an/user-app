package com.github.rybalkin_an.app.mockmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.controller.UserController;
import com.github.rybalkin_an.app.user.exception.NotFoundException;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("h2")
@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    private final String url = "/api/users";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void givenUser_whenCreateUser_thenUserCreated() throws Exception {
        User givenUser = new TestUser();
        String userJson = mapper.writeValueAsString(givenUser);
        when(userService.create(any(User.class))).thenReturn(givenUser);

        MvcResult result = this.mockMvc.perform(post(url)
                        .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int responseStatus = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();
        User createdUser = mapper.readValue(responseBody, User.class);

        assertThat(responseStatus).isEqualTo(201);
        validateUser(givenUser, createdUser);
    }

    @Test
    public void givenUserId_whenGetUserById_thenReturnUser() throws Exception {
        User givenUser = new TestUser();
        when(userService.findById(givenUser.getId())).thenReturn(givenUser);

        MvcResult result = this.mockMvc.perform(get(url+"/{id}", givenUser.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int responseStatus = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();
        User createdUser = mapper.readValue(responseBody, User.class);

        assertThat(responseStatus).isEqualTo(200);
        validateUser(createdUser, givenUser);
    }

    @Test
    public void givenNotValidUserId_whenGetUserById_thenReturn404() throws Exception {
        when(userService.findById(any(UUID.class))).thenThrow(new NotFoundException());

        MvcResult result = this.mockMvc.perform(get(url+"/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int responseStatus = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();

        assertThat(responseStatus).isEqualTo(404);
        assertThat(responseBody).isEqualTo("Resource ID not found.");
    }

    private void validateUser(User actualResult, User expectedResult){
        assertThat(expectedResult.getId()).isEqualTo(actualResult.getId());
        assertThat(expectedResult.getBirthdate()).isEqualTo(actualResult.getBirthdate());
        assertThat(expectedResult.getFirstName()).isEqualTo(actualResult.getFirstName());
        assertThat(expectedResult.getLastName()).isEqualTo(actualResult.getLastName());
        assertThat(expectedResult.getRegistrationDate()).isEqualTo(actualResult.getRegistrationDate());
        assertThat(expectedResult.getVersion()).isEqualTo(null);
    }
}
