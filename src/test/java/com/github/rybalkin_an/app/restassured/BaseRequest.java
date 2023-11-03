package com.github.rybalkin_an.app.restassured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("h2")
public class BaseRequest {

    @BeforeAll
    static void enableLogging(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
