package kr.kro.colla.project.project;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.*;
import kr.kro.colla.exception.exception.user.UserNotManagerException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.*;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserNoticeResponse;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
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
    private UserProvider user;

    @Autowired
    private ProjectProvider project;

    @Autowired
    private StoryProvider story;

    @Autowired
    private TagProvider tag;

    @Autowired
    private TaskStatusProvider taskStatus;

    @Autowired
    private NoticeProvider notice;

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
    void 프로젝트_목록에서_클릭한_프로젝트를_조회한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        ProjectResponse response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId())

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", equalTo(createdProject.getName()))
                .body("description", equalTo(createdProject.getDescription()))
                .body("members.size()", is(1))
                .extract()
                .body()
                .as(ProjectResponse.class);
        assertThat(response.getMembers().get(0).getGithubId()).isEqualTo(registeredUser.getGithubId());
        assertThat(response.getTasks().size()).isEqualTo(3);
        assertThat(response.getTasks().containsKey("To Do")).isTrue();
        assertThat(response.getTasks().containsKey("In Progress")).isTrue();
        assertThat(response.getTasks().containsKey("Done")).isTrue();
    }

    @Test
    void 사용자를_프로젝트에_초대하는데_성공한다() {
        // given
        User manager = user.가_로그인을_한다1();
        User member = user.가_로그인을_한다2();
        String accessToken = auth.토큰을_발급한다(manager.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest(member.getGithubId());

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(projectMemberRequest)

        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/members")

        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 사용자_초대를_권한_부족으로_실패한다() {
        // given
        User member = user.가_로그인을_한다1();
        String memberToken = auth.토큰을_발급한다(member.getId());
        User manager = user.가_로그인을_한다2();
        String managerToken = auth.토큰을_발급한다(manager.getId());
        UserProjectResponse createdProject = project.를_생성한다(managerToken);
        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest("member_github");

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", memberToken)
                .body(projectMemberRequest)
        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/members")
        // then
        .then()
                .statusCode(HttpStatus.FORBIDDEN.value())
                .body("status", equalTo(HttpStatus.FORBIDDEN.value()))
                .body("message", equalTo(new UserNotManagerException().getMessage()));

    }

    @Test
    void 사용자_초대를_githubId_부족으로_실패한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        ProjectMemberRequest projectMemberRequest = new ProjectMemberRequest();

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(projectMemberRequest)
        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/members")
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
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        String title = "story title";
        CreateStoryRequest createStoryRequest = new CreateStoryRequest(title);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(createStoryRequest)

        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/stories")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("title", equalTo(title));
    }

    @Test
    void 사용자가_프로젝트_스토리를_조회한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        String storyTitle = "story title";
        story.를_생성한다(createdProject.getId(), accessToken, storyTitle);

        List<ProjectStoryResponse> response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/stories")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectStoryResponse>>() {});

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getTitle()).isEqualTo(storyTitle);
    }

    @Test
    void 사용자가_프로젝트_초대를_수락한다() {
        // given
        User manager = user.가_로그인을_한다1();
        String managerToken = auth.토큰을_발급한다(manager.getId());
        User member = user.가_로그인을_한다2();
        String memberToken = auth.토큰을_발급한다(member.getId());

        UserProjectResponse createdProject = project.를_생성한다(managerToken);
        notice.를_생성한다(managerToken, createdProject.getId(), member.getGithubId());
        List<UserNoticeResponse> noticeResponseList = notice.를_조회한다(memberToken);

        ProjectMemberDecision projectMemberDecision = new ProjectMemberDecision(true, noticeResponseList.get(0).getId());

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", memberToken)
                .body(projectMemberDecision)

        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/members/decision")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(member.getId().intValue()))
                .body("name", equalTo(member.getName()))
                .body("avatar", equalTo(member.getAvatar()))
                .body("githubId", equalTo(member.getGithubId()));
    }

    @Test
    void 사용자가_프로젝트_초대를_거절한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        User member = user.가_로그인을_한다2();
        String memberToken = auth.토큰을_발급한다(member.getId());

        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        notice.를_생성한다(accessToken, createdProject.getId(), member.getGithubId());
        List<UserNoticeResponse> noticeResponseList = notice.를_조회한다(memberToken);

        ProjectMemberDecision projectMemberDecision = new ProjectMemberDecision(false, noticeResponseList.get(0).getId());

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", memberToken)
                .body(projectMemberDecision)
        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/members/decision")

        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 사용자가_프로젝트_멤버를_조회한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        List<ProjectMemberResponse> response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/members")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectMemberResponse>>() {});

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getId()).isEqualTo(registeredUser.getId());
        assertThat(response.get(0).getName()).isEqualTo(registeredUser.getName());
        assertThat(response.get(0).getAvatar()).isEqualTo(registeredUser.getAvatar());
    }

    @Test
    void 사용자가_프로젝트에서_사용할_새로운_태스크_태그를_생성한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        String tagName = "backend";
        CreateTagRequest createTagRequest = new CreateTagRequest(tagName);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(createTagRequest)

        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/tags")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(createTagRequest.getName()));
    }

    @Test
    void 사용자는_빈_태스크_태그를_생성할_수_없다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        CreateTagRequest createTagRequest = new CreateTagRequest("");

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(createTagRequest)

        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/tags")

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("공백일 수 없습니다"));
    }

    @Test
    void 사용자가_프로젝트에_등록되어_있는_테스크_태그들을_조회한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        ProjectTagResponse createdTag = tag.를_생성한다(accessToken, createdProject.getId(), "backend");

        List<ProjectTagResponse> response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)

        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/tags")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTagResponse>>() {});

        assertThat(response.size()).isEqualTo(1);
        assertThat(response.get(0).getName()).isEqualTo(createdTag.getName());
    }

    @Test
    void 사용자가_프로젝트의_테스크_상태값을_추가할_수_있다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        String statusName = "MY_TASK_STATUS_NAME_TO_CREATE";
        CreateTaskStatusRequest request = new CreateTaskStatusRequest(statusName);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(request)
        // when
        .when()
                .post("/api/projects/" + createdProject.getId() + "/statuses")
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("name", equalTo(statusName));
    }

    @Test
    void 사용자는_프로젝트_테스크_상태값을_삭제할_수_있다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        String statusName = "MY_STATUS_TO_DELETE", statusNameToChange = "MY_STATUS_TO_CHANGE";
        List<String> statuses = List.of("NEW_TO_DO_LIST", "NEW_DONE_LIST", statusName, statusNameToChange);
        statuses.forEach(name -> taskStatus.를_생성한다(accessToken, createdProject.getId(), name));

        DeleteTaskStatusRequest request = new DeleteTaskStatusRequest(statusName, statusNameToChange);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(request)
        // when
        .when()
                .delete("/api/projects/" + createdProject.getId() + "/statuses")
        // then
        .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void 사용자는_프로젝트_테스크_상태값을_조회할_수_있다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);

        List<ProjectTaskStatusResponse> response = given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
        // when
        .when()
                .get("/api/projects/" + createdProject.getId() + "/statuses")
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<ProjectTaskStatusResponse>>(){});
        assertThat(response.size()).isEqualTo(3);
        response
                .stream()
                .forEach(s -> assertThat(s.getId()).isNotNull());
        response
                .stream()
                .forEach(s -> assertThat(List.of("To Do","In Progress", "Done").contains(s.getName())));
    }
}
