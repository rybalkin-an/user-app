# Spring boot app REST API testing

### Prerequisites
Make sure you have the following tools installed:
* Docker [https://www.docker.com/get-started/](https://www.docker.com/get-started/)
* Java 17 
* Gradle 8

### Project Dependencies
* junit 5 [https://junit.org/junit5/](https://junit.org/junit5/)
* RestAssured [https://rest-assured.io/](https://rest-assured.io/)
* MockMvc [https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-framework.html](https://docs.spring.io/spring-framework/reference/testing/spring-mvc-test-framework.html)
* Testcontainers [https://java.testcontainers.org/](https://java.testcontainers.org/)
* Wiremock [https://wiremock.org/docs/junit-jupiter/](https://wiremock.org/docs/junit-jupiter/)

### Running Tests
Execute the following command to run tests:
```bash
gradle clean test
```

### Running the Application
Follow these steps to run the Spring Boot application:
1. Start Docker containers:
```bash
docker compose up
```
2. Run the Spring Boot application:
```bash
gradle bootRun
```
3. Access the API documentation in Swagger: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
