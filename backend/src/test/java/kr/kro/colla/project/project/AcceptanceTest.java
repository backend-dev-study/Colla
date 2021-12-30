package kr.kro.colla.project.project;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.kro.colla.user.user.controller.dto.CreateProjectRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

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
        CreateProjectRequest createProjectRequest = CreateProjectRequest.builder()
                .name(name)
                .description(desc)
                .build();
        String content = new ObjectMapper().writeValueAsString(createProjectRequest);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .param("name", name)
                .param("description",desc)

        // when
                .when()
                .post("/api/users/"+managerId+"/projects")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("managerId", equalTo(managerId))
                .body("name", equalTo(name))
                .body("description", equalTo(desc));
    }
}
