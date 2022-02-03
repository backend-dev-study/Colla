package kr.kro.colla.task.task;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.fixture.Auth;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.task_status.domain.repository.TaskStatusRepository;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    private Auth auth;
    private User user;
    private Project project;
    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        auth = new Auth(jwtProvider);

        user = User.builder()
                .name("yeongkee")
                .githubId("kykapple")
                .avatar("s3_content")
                .build();
        userRepository.save(user);

        project = Project.builder()
                .managerId(user.getId())
                .name("project name")
                .description("project description")
                .thumbnail("s3_content")
                .build();
        projectRepository.save(project);

        accessToken = auth.로그인(user.getId());
    }

    @AfterEach
    void rollback() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void 사용자가_프로젝트에_새로운_태스크를_추가한다() {
        // given
        Map<String, String> formData = new HashMap<>();
        formData.put("title", "task title");
        formData.put("priority", "3");
        formData.put("status", "To Do");
        formData.put("projectId", project.getId().toString());

        given()
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(formData)

        // when
        .when()
                .post("/api/projects/tasks")

        // then
        .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void 태스크_생성_시_필요한_데이터가_없을_경우_예외가_발생한다() {
        // given
        Map<String, String> formData = new HashMap<>();
        formData.put("title", "task title");
        formData.put("priority", "3");
        formData.put("status", "To Do");

        given()
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(formData)

        // when
        .when()
                .post("/api/projects/tasks")

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("projectId : 널이어서는 안됩니다"));
    }

}
