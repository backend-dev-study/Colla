package kr.kro.colla.task.task;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.*;
import kr.kro.colla.project.project.presentation.dto.ProjectStorySimpleResponse;
import kr.kro.colla.task.task.presentation.dto.*;
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
    private TagProvider tag;

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
        ProjectStorySimpleResponse createdStory = story.를_생성한다(createdProject.getId(), accessToken, "story title");
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
        ProjectStorySimpleResponse createdStory = story.를_생성한다(createdProject.getId(), accessToken, "story title");
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

        ProjectStorySimpleResponse createdStory = story.를_생성한다(createdProject.getId(), accessToken, "new story");
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
        ProjectStorySimpleResponse oldStory = story.를_생성한다(createdProject.getId(), accessToken, "old story");
        Map<String, String> createdTask = task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), oldStory.getTitle());

        ProjectStorySimpleResponse newStory = story.를_생성한다(createdProject.getId(), accessToken, "new story");
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
                .patch("/api/projects/" + createdProject.getId() + "/tasks/" + 1L)
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
                .patch("/api/projects/" + createdProject.getId() + "/tasks/" + 1L)
        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", equalTo("statusName : 널이어서는 안됩니다"));
    }

    @Test
    void 사용자가_스토리에_속한_태스크들을_조회한다() {
        // given
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        ProjectStorySimpleResponse createdStory = story.를_생성한다(createdProject.getId(), accessToken, "story title");
        task.를_특정_제목과_함께_생성한다(accessToken, member.getId(), createdProject.getId(), createdStory.getTitle(), "first task");
        task.를_특정_제목과_함께_생성한다(accessToken, null, createdProject.getId(), null, "second task");
        task.를_특정_제목과_함께_생성한다(accessToken, member.getId(), createdProject.getId(), createdStory.getTitle(), "third task");

        List<RoadmapTaskResponse> response = given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/stories/" + 1L + "/tasks")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<RoadmapTaskResponse>>() {});

        assertThat(response).hasSize(2);
        assertThat(response.get(0).getTitle()).isEqualTo("first task");
        assertThat(response.get(1).getTitle()).isEqualTo("third task");
    }

    @Test
    void 사용자는_프로젝트의_테스크들을_생성_날짜_오름차순으로_조회할_수_있다() {
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
    void 사용자가_프로젝트의_태스크들을_우선순위_오름차순으로_조회한다() {
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
                .get("/api/projects/ " + createdProject.getId() + "/tasks/priority?ascending=true")

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
    void 사용자가_프로젝트의_태스크들을_우선순위_내림차순으로_조회한다() {
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

    @Test
    void 사용자가_프로젝트의_테스크들을_상태값들로_필터링한다() {
        // given
        String nameToFilter1 = "sTatuSToFiLteR", nameToFilter2 = "mUltiPleStaTuS", nameToIgnore = "sTatUStOiGNOre";
        User loginUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        taskStatus.를_생성한다(accessToken, createdProject.getId(), nameToFilter1);
        taskStatus.를_생성한다(accessToken, createdProject.getId(), nameToFilter2);
        taskStatus.를_생성한다(accessToken, createdProject.getId(), nameToIgnore);

        task.를_특정_상태로_생성한다(accessToken, loginUser.getId(), createdProject.getId(), null, nameToIgnore);
        List<Map<String, String>> filteredTasks = List.of(
                task.를_특정_상태로_생성한다(accessToken, loginUser.getId(), createdProject.getId(), null, nameToFilter1),
                task.를_특정_상태로_생성한다(accessToken, loginUser.getId(), createdProject.getId(), null, nameToFilter2)
        );
        task.를_특정_상태로_생성한다(accessToken, null, createdProject.getId(), null, nameToIgnore);

        List<ProjectTaskSimpleResponse> result = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/statuses?statuses="+nameToFilter1+", "+nameToFilter2)

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskSimpleResponse>>() {});

        assertThat(result.size()).isEqualTo(filteredTasks.size());
        result.forEach(task -> {
            assertThat(task.getId()).isNotNull();
            assertThat(List.of(nameToFilter1, nameToFilter2).contains(task.getStatus()));
        });
    }

    @Test
    void 사용자는_상태값_이름_없이_프로젝트의_테스크들을_상태값들로_필터링할_수_없다() {
        // given
        User loginUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/statuses")

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void 사용자가_특정_태그들을_선택해_태스크들을_필터링한다() {
        // given
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        tag.를_생성한다(accessToken, createdProject.getId(), "backend");
        tag.를_생성한다(accessToken, createdProject.getId(), "documentation");
        tag.를_생성한다(accessToken, createdProject.getId(), "enhancement");
        tag.를_생성한다(accessToken, createdProject.getId(), "bug fix");

        task.를_특정_태그와_함께_생성한다(accessToken, member.getId(), createdProject.getId(), null, "[\"backend\", \"bug fix\", \"enhancement\"]");
        task.를_특정_태그와_함께_생성한다(accessToken, member.getId(), createdProject.getId(), null, "[\"frontend\", \"refactoring\", \"bug fix\"]");
        task.를_특정_태그와_함께_생성한다(accessToken, member.getId(), createdProject.getId(), null, "[\"documentation\", \"backend\", \"bug fix\", \"refactoring\"]");
        task.를_특정_태그와_함께_생성한다(accessToken, member.getId(), createdProject.getId(), null, "[\"backend\"]");

        List<ProjectTaskSimpleResponse> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/tags?tags=bug fix,backend")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskSimpleResponse>>() {});

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getTags()).contains("bug fix", "backend");
        assertThat(response.get(1).getTags()).contains("bug fix", "backend");
    }

    @Test
    void 사용자가_아무_태스크도_가지고_있지_않은_태그를_선택해_필터링한다() {
        // given
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        tag.를_생성한다(accessToken, createdProject.getId(), "backend");
        tag.를_생성한다(accessToken, createdProject.getId(), "documentation");
        tag.를_생성한다(accessToken, createdProject.getId(), "enhancement");
        tag.를_생성한다(accessToken, createdProject.getId(), "bug fix");

        task.를_특정_태그와_함께_생성한다(accessToken, member.getId(), createdProject.getId(), null, "[\"frontend\", \"refactoring\"]");
        task.를_특정_태그와_함께_생성한다(accessToken, member.getId(), createdProject.getId(), null, "[\"backend\", \"enhancement\", \"bug fix\"]");

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/tags?tags=documentation")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(0));
    }

    @Test
    void 사용자가_프로젝트의_태스크들을_스토리로_그룹핑한다() {
        // given
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        ProjectStorySimpleResponse story1 = story.를_생성한다(createdProject.getId(), accessToken, "user can login with github");
        ProjectStorySimpleResponse story2 = story.를_생성한다(createdProject.getId(), accessToken, "set up CI/CD");

        task.를_생성한다(accessToken, member.getId(), createdProject.getId(), story1.getTitle());
        task.를_생성한다(accessToken, member.getId(), createdProject.getId(), null);
        task.를_생성한다(accessToken, member.getId(), createdProject.getId(), story2.getTitle());
        task.를_생성한다(accessToken, member.getId(), createdProject.getId(), story1.getTitle());

        List<ProjectStoryTaskResponse> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/story")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectStoryTaskResponse>>() {});

        assertThat(response).hasSize(3);
        assertThat(response.get(0).getStory()).isEqualTo(story1.getTitle());
        assertThat(response.get(0).getTaskList()).hasSize(2);
        assertThat(response.get(1).getStory()).isEqualTo(story2.getTitle());
        assertThat(response.get(1).getTaskList()).hasSize(1);
        assertThat(response.get(2).getStory()).isNull();
        assertThat(response.get(2).getTaskList()).hasSize(1);
    }

    @Test
    void 사용자가_프로젝트의_테스크들을_담당자로_필터링한다() {
        // given
        User manager1 = user.가_로그인을_한다2();
        User manager2 = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(manager1.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_생성한다(accessToken, manager1.getId(), createdProject.getId(), null);
        task.를_생성한다(accessToken, manager2.getId(), createdProject.getId(), null);
        task.를_생성한다(accessToken, manager2.getId(), createdProject.getId(), null);

        List<ProjectTaskSimpleResponse> result = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/managers?managers=" + manager1.getId() + ", " + manager2.getId())

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskSimpleResponse>>() {});

        assertThat(result.size()).isEqualTo(3);
        result.forEach(task -> {
            assertThat(task.getId()).isNotNull();
            assertThat(List.of(manager1.getName(), manager2.getName()).contains(task.getManagerName()));
            assertThat(List.of(manager1.getAvatar(), manager2.getAvatar()).contains(task.getManagerAvatar()));
        });
    }

    @Test
    void 사용자가_프로젝트의_담당자_없는_테스크를_포함해_담당자들로_필터링한다() {
        // given
        User loginUser = user.가_로그인을_한다2();
        User manager = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_생성한다(accessToken, loginUser.getId(), createdProject.getId(), null);
        task.를_생성한다(accessToken, null, createdProject.getId(), null);
        task.를_생성한다(accessToken, null, createdProject.getId(), null);
        task.를_생성한다(accessToken, manager.getId(), createdProject.getId(), null);

        List<ProjectTaskSimpleResponse> result = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/managers?managers=" + manager.getId() + "&notSelected=true")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskSimpleResponse>>() {});

        assertThat(result.size()).isEqualTo(3);
        assertThat(result.stream()
                .filter(task->task.getManagerName()==null)
                .count()).isNotZero();
        result.forEach(task -> {
            assertThat(task.getId()).isNotNull();
            assertThat(Arrays.asList(null, manager.getName()).contains(task.getManagerName()));
            assertThat(Arrays.asList(null, manager.getAvatar()).contains(task.getManagerAvatar()));
        });
    }

    @Test
    void 사용자가_특정_키워드로_태스크들을_조회한다() {
        // given
        String keyword = "api documentation";
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_특정_제목과_함께_생성한다(accessToken, member.getId(), createdProject.getId(), null, "Write api documentation");
        task.를_특정_제목과_함께_생성한다(accessToken, member.getId(), createdProject.getId(), null, "Improving query performance");
        task.를_특정_제목과_함께_생성한다(accessToken, null, createdProject.getId(), null, "Decorate api documentation");

        List<ProjectTaskSimpleResponse> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/search?keyword=" + keyword)

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskSimpleResponse>>() {});

        assertThat(response).hasSize(2);
        response.forEach(task -> assertThat(task.getTitle()).contains(keyword));
    }

    @Test
    void 사용자가_존재하지_않는_키워드로_태스크를_검색하면_아무것도_반환하지_않는다() {
        // given
        String keyword = "documentation";
        User member = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(member.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_특정_제목과_함께_생성한다(accessToken, member.getId(), createdProject.getId(), null, "implement backlog task search api");
        task.를_특정_제목과_함께_생성한다(accessToken, null, createdProject.getId(), null, "refactor backlog filter api");

        List<ProjectTaskSimpleResponse> response = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/search?keyword=" + keyword)

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskSimpleResponse>>() {});

        assertThat(response.size()).isZero();
    }

    @Test
    void 사용자는_상태값_별로_테스크_개수를_조회할_수_있다() {
        // given
        List<String> statusNames = List.of("Before Start", "Progress", "After Done");
        User loginUser = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        statusNames.forEach((name)-> {
            taskStatus.를_생성한다(accessToken, createdProject.getId(), name);
            task.를_특정_상태로_생성한다(accessToken, loginUser.getId(), createdProject.getId(), null, name);
        });

        List<TaskCntResponse> result = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/count")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<TaskCntResponse>>() {});

        assertThat(result.size()).isEqualTo(statusNames.size());
        result.forEach((response)->{
            assertThat(response.getTaskCnt()).isEqualTo(1);
            assertThat(statusNames).contains(response.getTaskStatusName());
        });
    }

    @Test
    void 사용자는_담당자에_따라_상태값_별로_테스크_개수를_조회할_수_있다() {
        // given
        List<String> statusNames = List.of("Before Start", "Progress");
        User loginUser = user.가_로그인을_한다2();
        User managerUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        statusNames.forEach((name)-> {
            taskStatus.를_생성한다(accessToken, createdProject.getId(), name);
            task.를_특정_상태로_생성한다(accessToken, managerUser.getId(), createdProject.getId(), null, name);
            task.를_특정_상태로_생성한다(accessToken, managerUser.getId(), createdProject.getId(), null, name);
        });

        List<ManagerTaskCntResponse> result = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/count/manager")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ManagerTaskCntResponse>>() {});

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getManagerName()).isEqualTo(managerUser.getName());
        assertThat(result.get(0).getTaskCnts().size()).isEqualTo(statusNames.size());
        assertThat(result.get(0).getTaskCnts().get(0).getTaskCnt()).isEqualTo(2);
    }

    @Test
    void 사용자는_담당자가_없는_테스크들도_상태값_별로_개수를_조회할_수_있다() {
        // given
        List<String> statusNames = List.of("Before Start", "Progress");
        User loginUser = user.가_로그인을_한다2();
        User managerUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(loginUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        statusNames.forEach((name)-> {
            taskStatus.를_생성한다(accessToken, createdProject.getId(), name);
            task.를_특정_상태로_생성한다(accessToken, null, createdProject.getId(), null, name);
        });

        List<ManagerTaskCntResponse> result = given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tasks/count/manager")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ManagerTaskCntResponse>>() {});

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getManagerName()).isEqualTo("담당자 없음");
        assertThat(result.get(0).getTaskCnts().size()).isEqualTo(statusNames.size());
        assertThat(result.get(0).getTaskCnts().get(0).getTaskCnt()).isEqualTo(1);
    }
}
