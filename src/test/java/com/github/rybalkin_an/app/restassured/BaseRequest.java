package com.github.rybalkin_an.app.restassured;

import com.github.rybalkin_an.app.Application;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
public class BaseRequest {

    @LocalServerPort
    protected int randomServerPort;

    @BeforeAll
    static void enableLogging(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
