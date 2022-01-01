package kr.kro.colla.project.project;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private Long managerId = 3L;
    private String name = "프로젝트 이름", desc = "프로젝트 설명";

    @Test
    void 사용자_프로젝트_생성_후_반환() throws Exception {
        // given
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("description",desc);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
        // when
        .when()
                .post("/api/users/{userId}/projects",managerId)
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("managerId", equalTo(managerId.intValue()))
                .body("name", equalTo(name))
                .body("description", equalTo(desc));
    }
}
