package kr.kro.colla.task.task;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.*;
import kr.kro.colla.project.project.presentation.dto.ProjectStoryResponse;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskSimpleResponse;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskRequest;
import kr.kro.colla.task.task.presentation.dto.UpdateTaskStatusRequest;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
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
    private TaskStatusProvider taskStatus;

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
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        Map<String, String> formData = new HashMap<>();
        formData.put("title", "task title");
        formData.put("priority", "3");
        formData.put("status", "To Do");
        formData.put("tags", "[]");
        formData.put("story", "");
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
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());

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
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        ProjectStoryResponse createdStory = story.를_생성한다(createdProject.getId(), accessToken, "story title");
        Map<String, String> createdTask = task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), createdStory.getTitle());

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/tasks/" + 1L)

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(createdTask.get("title")))
                .body("description", equalTo(createdTask.get("description")))
                .body("manager", equalTo(registeredUser.getName()))
                .body("story", equalTo(createdStory.getTitle()))
                .body("status", equalTo(createdTask.get("status")))
                .body("priority", equalTo(Integer.parseInt(createdTask.get("priority"))));
    }

    @Test
    void 사용자가_담당자와_스토리가_없는_태스크를_조회한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        Map<String, String> createdTask = task.를_생성한다(accessToken, null, createdProject.getId(), null);

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/tasks/" + 1L)

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(createdTask.get("title")))
                .body("manager", nullValue())
                .body("story", nullValue());
    }

    @Test
    void 사용자가_태스크의_내용을_수정한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        ProjectStoryResponse createdStory = story.를_생성한다(createdProject.getId(), accessToken, "story title");
        task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), createdStory.getTitle());

        Map<String, String> formData = new HashMap<>();
        formData.put("title", "new title");
        formData.put("managerId", registeredUser.getName());
        formData.put("description", "new description");
        formData.put("story", createdStory.getTitle());
        formData.put("priority", "3");
        formData.put("projectId", createdProject.getId().toString());
        formData.put("tags", "[\"backend\"]");

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(formData)

        // when
        .when()
                .put("/api/projects/tasks/" + 1L)

        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 사용자가_스토리에_속해있지_않은_태스크에_스토리를_추가한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        Map<String, String> createdTask = task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), null);

        ProjectStoryResponse createdStory = story.를_생성한다(createdProject.getId(), accessToken, "new story");
        createdTask.put("story", createdStory.getTitle());

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(createdTask)

        // when
        .when()
                .put("/api/projects/tasks/" + 1L)

        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 사용자가_특정_스토리에_속해있는_태스크의_스토리를_수정한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        ProjectStoryResponse oldStory = story.를_생성한다(createdProject.getId(), accessToken, "old story");
        Map<String, String> createdTask = task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), oldStory.getTitle());

        ProjectStoryResponse newStory = story.를_생성한다(createdProject.getId(), accessToken, "new story");
        createdTask.put("story", newStory.getTitle());

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(createdTask)

        // when
        .when()
                .put("/api/projects/tasks/" + 1L)

        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 스토리에_속해있지_않은_태스크에_스토리를_추가하지도_않으면_아무런_반영도_되지_않는다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        Map<String, String> createdTask = task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), null);

        createdTask.put("story", "");

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.URLENC)
                .cookie("accessToken", accessToken)
                .formParams(createdTask)

        // when
        .when()
                .put("/api/projects/tasks/" + 1L)

        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 사용자가_테스크의_상태값을_수정한다() {
        // given
        String newStatusName = "새로 변경될 상태 이름!!";

        User loginedUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginedUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_생성한다(accessToken, null, createdProject.getId(), null);
        taskStatus.를_생성한다(accessToken, createdProject.getId(), newStatusName);

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest(newStatusName);

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(request)
        // when
        .when()
                .patch("/api/projects/tasks/" + 1L)
        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 사용자는_상태_이름_없이_테스크_상태를_수정할_수_없다() {
        // given
        User loginUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_생성한다(accessToken, null, createdProject.getId(), null);

        UpdateTaskStatusRequest request = new UpdateTaskStatusRequest();

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(request)
        // when
        .when()
                .patch("/api/projects/tasks/" + 1L)
        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("statusName : 널이어서는 안됩니다"));
    }

    @Test
    void 사용자는_프로젝트의_테스크를_생성_날짜_오름차순으로_조회할_수_있다() {
        // given
        User loginUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_생성한다(accessToken, loginUser.getId(), createdProject.getId(), null);
        task.를_생성한다(accessToken, null, createdProject.getId(), null);

        List<ProjectTaskSimpleResponse> responses = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/created-date?ascending=true")

        // then
        .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskSimpleResponse>>() {});

        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.stream()
                        .map(ProjectTaskSimpleResponse::getManagerName)
                        .collect(Collectors.toList()).containsAll(Arrays.asList(loginUser.getName(), null)));
    }
    
    @Test
    void 사용자가_프로젝트의_태스크를_우선순위_오름차순으로_조회한다() {
        // given
        User member1 = user.가_로그인을_한다1();
        User member2 = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(member1.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_특정_우선순위로_생성한다(accessToken, member1.getId(), createdProject.getId(), null, 2);
        task.를_특정_우선순위로_생성한다(accessToken, member1.getId(), createdProject.getId(), null, 5);
        task.를_특정_우선순위로_생성한다(accessToken, member2.getId(), createdProject.getId(), null, 1);

        ProjectTaskRequest projectTaskRequest = new ProjectTaskRequest(createdProject.getId());

        List<ProjectTaskSimpleResponse> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(projectTaskRequest)

        // when
        .when()
                .get("/api/projects/ "+ createdProject.getId() + "/tasks/priority?ascending=true")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskSimpleResponse>>() {});

        assertThat(response.size()).isEqualTo(3);
        IntStream.range(0, response.size() - 1)
                .forEach(idx -> assertThat(response.get(idx).getPriority()).isLessThan(response.get(idx + 1).getPriority()));
    }

    @Test
    void 사용자가_프로젝트의_태스크를_우선순위_내림차순으로_조회한다() {
        // given
        User member1 = user.가_로그인을_한다1();
        User member2 = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(member1.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_특정_우선순위로_생성한다(accessToken, member1.getId(), createdProject.getId(), null, 3);
        task.를_특정_우선순위로_생성한다(accessToken, member2.getId(), createdProject.getId(), null, 1);
        task.를_특정_우선순위로_생성한다(accessToken, member2.getId(), createdProject.getId(), null, 5);

        ProjectTaskRequest projectTaskRequest = new ProjectTaskRequest(createdProject.getId());

        List<ProjectTaskSimpleResponse> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(projectTaskRequest)

        // when
        .when()
                .get("/api/projects/ "+ createdProject.getId() + "/tasks/priority")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskSimpleResponse>>() {});

        assertThat(response.size()).isEqualTo(3);
        IntStream.range(0, response.size() - 1)
                .forEach(idx -> assertThat(response.get(idx).getPriority()).isGreaterThan(response.get(idx + 1).getPriority()));
    }

}
