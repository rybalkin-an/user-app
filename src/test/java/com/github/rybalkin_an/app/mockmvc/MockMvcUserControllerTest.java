package com.github.rybalkin_an.app.mockmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.controller.UserController;
import com.github.rybalkin_an.app.user.exception.NotFoundException;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class MockMvcUserControllerTest {

    private final String url = "/api/users";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;


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
    public void givenUserWithoutFirstName_whenCreateUser_then400() throws Exception {
        User givenUser = new TestUser();
        givenUser.setFirstName(null);
        String userJson = mapper.writeValueAsString(givenUser);

        this.mockMvc.perform(post(url)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(result.getResponse().getContentAsString())
                        .contains("Firstname should be not empty"));
    }

    @Test
    public void givenUserId_whenGetUserById_thenReturnUser() throws Exception {
        User givenUser = new TestUser();
        when(userService.findById(givenUser.getId())).thenReturn(givenUser);

        MvcResult result = this.mockMvc.perform(get(url + "/{id}", givenUser.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int responseStatus = result.getResponse().getStatus();
        String responseBody = result.getResponse().getContentAsString();
        User createdUser = mapper.readValue(responseBody, User.class);

        assertThat(responseStatus).isEqualTo(200);
        validateUser(givenUser, createdUser);
    }

    @Test
    public void givenNotValidUserId_whenGetUserById_thenReturn404() throws Exception {
        when(userService.findById(any(UUID.class))).thenThrow(new NotFoundException());

        MvcResult result = this.mockMvc.perform(get(url+"/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int responseStatus = result.getResponse().getStatus();

        assertThat(responseStatus).isEqualTo(404);
    }

    private void validateUser(User givenUser, User createdUser){
        assertThat(createdUser.getId()).isEqualTo(givenUser.getId());
        assertThat(createdUser.getBirthdate()).isEqualTo(givenUser.getBirthdate());
        assertThat(createdUser.getFirstName()).isEqualTo(givenUser.getFirstName());
        assertThat(createdUser.getLastName()).isEqualTo(givenUser.getLastName());
        assertThat(createdUser.getRegistrationDate()).isEqualTo(givenUser.getRegistrationDate());
        assertThat(createdUser.getVersion()).isEqualTo(null);
    }
}
