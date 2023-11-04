package com.github.rybalkin_an.app.testcontainers;

import com.github.rybalkin_an.app.restassured.user.steps.UserApi;
import com.github.rybalkin_an.app.testdata.TestUser;
import com.github.rybalkin_an.app.user.model.User;
import com.github.rybalkin_an.app.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserRepositoryPostgresTest {

    @LocalServerPort
    private int randomServerPort;

    private UserApi userApi;

    @Autowired
    private UserRepository userRepository;

    @Container
    private static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest");

    @BeforeEach
    public void setUp() {
        userApi = new UserApi(randomServerPort);
    }

    @AfterEach
    public void teardown(){
        this.userRepository.deleteAll();
        postgresContainer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @ParameterizedTest(name = "User repository test with {0}")
    @ValueSource(strings = {"postgres:16", "postgres:15", "postgres:13"})
    void testPostgresVersions(String postgresVersion) {
        postgresContainer = new PostgreSQLContainer<>(postgresVersion);
        postgresContainer.start();

        User givenUser = new TestUser();
        this.userApi.create(givenUser);
        List<User> userList = this.userRepository.findAll();
        assertThat(userList).hasSize(1);
    }

}