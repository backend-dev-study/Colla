package kr.kro.colla.project.project;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.fixture.Auth;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.project.presentation.dto.*;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.domain.repository.StoryRepository;
import kr.kro.colla.task.tag.domain.repository.TagRepository;
import kr.kro.colla.task.task_tag.domain.repository.TaskTagRepository;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.domain.repository.UserProjectRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TaskTagRepository taskTagRepository;

    private Auth auth;
    private User user;
    private Project project;
    private String accessToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        user = User.builder()
                .githubId("kykapple")
                .name("kyk")
                .avatar("github_content")
                .build();
        userRepository.save(user);

        project = Project.builder()
                .managerId(user.getId())
                .name("project name")
                .description("project description")
                .thumbnail("s3_content")
                .build();
        projectRepository.save(project);

        UserProject userProject = UserProject.builder()
                .user(user)
                .project(project)
                .build();
        userProjectRepository.save(userProject);

        auth = new Auth(jwtProvider);
        accessToken = auth.로그인(user.getId());
    }

    @AfterEach
    void rollback(){
        taskTagRepository.deleteAll();
        tagRepository.deleteAll();
        storyRepository.deleteAll();
        userProjectRepository.deleteAll();
        userRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void 프로젝트_목록에서_클릭한_프로젝트를_조회한다() {
        // given
        ProjectResponse response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + project.getId())

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", equalTo(project.getName()))
                .body("description", equalTo(project.getDescription()))
                .body("members.size()", is(1))
                .extract()
                .body()
                .as(ProjectResponse.class);
        assertThat(response.getMembers().get(0).getGithubId()).isEqualTo(user.getGithubId());
        assertThat(response.getTasks().size()).isEqualTo(3);
        assertThat(response.getTasks().containsKey("To Do")).isTrue();
        assertThat(response.getTasks().containsKey("In Progress")).isTrue();
        assertThat(response.getTasks().containsKey("Done")).isTrue();
    }

    @Test
    void 사용자를_프로젝트에_초대하는데_성공한다() {
        // given
        User member = User.builder()
                .name("subin")
                .githubId("binimini")
                .avatar("profile")
                .build();
        userRepository.save(member);
        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest(member.getGithubId());

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(projectMemberRequest)
        // when
        .when()
                .post("/api/projects/" + project.getId() + "/members")
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .log();
    }

    @Test
    void 사용자_초대를_githubId_부족으로_실패한다() {
        // given
        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest();

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(projectMemberRequest)
        // when
        .when()
                .post("/api/projects/" + project.getId() + "/members")
        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(HttpStatus.BAD_REQUEST.value()))
                .body("message", containsString("githubId"))
                .body("message", containsString("비어 있을 수 없습니다"));
    }

    @Test
    void 사용자가_프로젝트_스토리를_생성한다() {
        // given
        String title = "story title";
        CreateStoryRequest createStoryRequest = new CreateStoryRequest(title);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(createStoryRequest)

        // when
        .when()
                .post("/api/projects/" + project.getId() + "/stories")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(title));
    }

    @Test
    void 사용자가_프로젝트_초대를_수락한다() {
        // given
        ProjectMemberDecision projectMemberDecision = new ProjectMemberDecision(true);
        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(projectMemberDecision)

        // when
        .when()
                .post("/api/projects/" + project.getId() + "/members/decision")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(user.getId().intValue()))
                .body("name", equalTo(user.getName()))
                .body("avatar", equalTo(user.getAvatar()))
                .body("githubId", equalTo(user.getGithubId()));
    }

    @Test
    void 사용자가_프로젝트_스토리를_조회한다() {
        // given
        Story story = Story.builder()
                .title("story title")
                .preStories("[]")
                .project(project)
                .build();
        storyRepository.save(story);

        List<ProjectStoryResponse> response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + project.getId() + "/stories")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectStoryResponse>>() {});

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getTitle()).isEqualTo(story.getTitle());
    }

    @Test
    void 사용자가_프로젝트_초대를_거절한다() {
        // given
        ProjectMemberDecision projectMemberDecision = new ProjectMemberDecision(false);
        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(projectMemberDecision)
        // when
        .when()
                .post("/api/projects/" + project.getId() + "/members/decision")

        // then
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 사용자가_프로젝트_멤버를_조회한다() {
        // given
        List<ProjectMemberResponse> response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + project.getId() + "/members")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectMemberResponse>>() {});

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getId()).isEqualTo(user.getId());
        assertThat(response.get(0).getName()).isEqualTo(user.getName());
        assertThat(response.get(0).getAvatar()).isEqualTo(user.getAvatar());
    }

    @Test
    void 사용자가_프로젝트에서_사용할_새로운_태스크_태그를_생성한다() {
        // given
        String tagName = "backend";
        CreateTagRequest createTagRequest = new CreateTagRequest(tagName);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(createTagRequest)

        // when
        .when()
                .post("/api/projects/" + project.getId() + "/tags")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(createTagRequest.getName()));
    }

    @Test
    void 사용자는_빈_태스크_태그를_생성할_수_없다() {
        // given
        CreateTagRequest createTagRequest = new CreateTagRequest("");

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(createTagRequest)

        // when
        .when()
                .post("/api/projects/" + project.getId() + "/tags")

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("공백일 수 없습니다"));

    }

}
