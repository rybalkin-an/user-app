package com.github.rybalkin_an.app.testcontainers;

import org.springframework.boot.devtools.restart.RestartScope;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {

    @Bean
    private WireMockContainer wireMockContainer(DynamicPropertyRegistry registry) {
        WireMockContainer container = new WireMockContainer("wiremock/wiremock:3.1.0-1")
                .withMappingFromResource("todo", "stubs/todo-v0-stub.json");
        registry.add("external.api.url", container::getBaseUrl);
        return container;
    }

    @Bean
    @RestartScope
    @ServiceConnection
    private PostgreSQLContainer<?> postgreSQLContainer(DynamicPropertyRegistry registry) {
        PostgreSQLContainer container = new PostgreSQLContainer<>("postgres:16");
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        return container;
    }
}
