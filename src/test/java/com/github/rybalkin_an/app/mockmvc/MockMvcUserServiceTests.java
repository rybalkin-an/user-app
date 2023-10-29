package com.github.rybalkin_an.app.mockmvc;

import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.controller.UserController;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.repository.UserRepository;
import com.github.rybalkin_an.app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test-application.properties")
class MockMvcUserServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testDeleteUser() throws Exception {
        User user = new TestUser();

        UUID userId = user.getId();

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        mockMvc.perform(delete("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(userId);
    }

    @Test
    public void testDeleteNotExistingUserUser() throws Exception {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        MvcResult result = mockMvc.perform(delete("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int responseStatus = result.getResponse().getStatus();
        System.out.println("Response Status: " + responseStatus);

        assertThat(responseStatus).isEqualTo(404);
    }

}
