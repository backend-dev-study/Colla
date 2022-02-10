package kr.kro.colla.task.task;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.*;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.user.user.domain.User;
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
import static org.hamcrest.CoreMatchers.nullValue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserProvider user;

    @Autowired
    private ProjectProvider project;

    @Autowired
    private StoryProvider story;

    @Autowired
    private TaskProvider task;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    private Auth auth;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        auth = new Auth(jwtProvider);
        databaseCleaner.execute();
    }

    @Test
    void 사용자가_프로젝트에_새로운_태스크를_추가한다() {
        // given
        User registeredUser = user.가_회원가입을_한다();
        String accessToken = auth.로그인(registeredUser.getId());
        Project createdProject = project.를_생성한다(registeredUser.getId());

        Map<String, String> formData = new HashMap<>();
        formData.put("title", "task title");
        formData.put("priority", "3");
        formData.put("status", "To Do");
        formData.put("tags", "[]");
        formData.put("projectId", createdProject.getId().toString());

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
        User registeredUser = user.가_회원가입을_한다();
        String accessToken = auth.로그인(registeredUser.getId());

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

    @Test
    void 사용자가_프로젝트에_속한_태스크를_조회한다() {
        // given
        User registeredUser = user.가_회원가입을_한다();
        String accessToken = auth.로그인(registeredUser.getId());
        Project createdProject = project.를_생성한다(registeredUser.getId());
        Story createdStory = story.를_생성한다(createdProject, "story title");
        Task createdTask = task.를_생성한다(registeredUser.getId(), createdProject, createdStory);

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/tasks/" + createdTask.getId())

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(createdTask.getTitle()))
                .body("description", equalTo(createdTask.getDescription()))
                .body("manager", equalTo(registeredUser.getName()))
                .body("story", equalTo(createdStory.getTitle()))
                .body("status", equalTo(createdTask.getTaskStatus().getName()))
                .body("priority", equalTo(createdTask.getPriority()));
    }

    @Test
    void 사용자가_담당자와_스토리가_없는_태스크를_조회한다() {
        // given
        User registeredUser = user.가_회원가입을_한다();
        String accessToken = auth.로그인(registeredUser.getId());
        Project createdProject = project.를_생성한다(registeredUser.getId());
        Task createdTask = task.를_생성한다(null, createdProject, null);

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/tasks/" + createdTask.getId())

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(createdTask.getTitle()))
                .body("manager", nullValue())
                .body("story", nullValue());
    }

    @Test
    void 사용자가_태스크의_내용을_수정한다() {
        // given
        User registeredUser = user.가_회원가입을_한다();
        String accessToken = auth.로그인(registeredUser.getId());
        Project createdProject = project.를_생성한다(registeredUser.getId());
        Story createdStory = story.를_생성한다(createdProject, "story title");
        Task createdTask = task.를_생성한다(registeredUser.getId(), createdProject, createdStory);

        Map<String, String> formData = new HashMap<>();
        formData.put("title", "new title");
        formData.put("managerId", registeredUser.getName());
        formData.put("description", "new description");
        formData.put("story", createdStory.getTitle());
        formData.put("priority", "3");
        formData.put("projectId", createdProject.getId().toString());
        formData.put("tags", "[\"backend\"]");

        given()
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(formData)

        // when
        .when()
                .put("/api/projects/tasks/" + createdTask.getId())

        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

}
