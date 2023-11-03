package com.github.rybalkin_an.app.restassured;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.http.ContentType.JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("h2")
public class BaseRequest {

    private final String url = "http://localhost/api";

    protected RequestSpecification requestSpec = new RequestSpecBuilder()
            .setContentType(JSON)
            .setAccept(JSON)
            .setBaseUri(url)
            .build();

    @BeforeAll
    static void enableLogging(){
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}
