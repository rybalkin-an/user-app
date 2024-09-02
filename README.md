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

---

### Installing Keycloak with Docker and Importing Realm Configuration
### Step 1: Run Keycloak with Docker
1. Pull the latest Keycloak Docker image:

```bash
docker pull quay.io/keycloak/keycloak:latest
```
2. Start the Keycloak container:
```bash
docker run -d --name keycloak \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -p 8080:8080 \
  quay.io/keycloak/keycloak:latest start-dev
```
3. Access Keycloak at http://localhost:8080/ and log in using the credentials admin / admin.

### Step 2: Import Realm Configuration
#### Manual import via Admin Console

1. Access the Keycloak Admin Console at http://localhost:8080/.
2. Select "Add Realm" and upload your realm-export.json file.
3. Click "Create" to complete the import.

---

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
3. Access the API documentation in Swagger: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
4. Access the UI: [http://localhost:8081/users/manage](http://localhost:8081/users/manage)

---

### Running Tests
Execute the following command to run tests:
```bash
gradle clean test
```