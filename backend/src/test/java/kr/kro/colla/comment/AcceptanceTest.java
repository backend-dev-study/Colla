package kr.kro.colla.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.comment.presentation.dto.CreateCommentRequest;
import kr.kro.colla.comment.presentation.dto.CreateCommentResponse;
import kr.kro.colla.comment.presentation.dto.TaskCommentResponse;
import kr.kro.colla.comment.presentation.dto.UpdateCommentRequest;
import kr.kro.colla.common.database.DatabaseCleaner;
import kr.kro.colla.common.fixture.*;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserProvider user;

    @Autowired
    private ProjectProvider project;

    @Autowired
    private TaskProvider task;

    @Autowired
    private CommentProvider comment;

    @Autowired
    private DatabaseCleaner databaseCleaner;

    @Autowired
    private ObjectMapper objectMapper;

    private Auth auth;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        auth = new Auth(jwtProvider);
        databaseCleaner.execute();
    }

    @Test
    void 사용자가_태스크에_댓글을_등록한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), null);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(null, "comment contents");

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(createCommentRequest)

        // when
        .when()
                .post("/api/tasks/" + 1L + "/comments")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("writer.displayName", equalTo(registeredUser.getName()))
                .body("writer.avatar", equalTo(registeredUser.getAvatar()))
                .body("superCommentId", nullValue())
                .body("contents", equalTo(createCommentRequest.getContents()));
    }

    @Test
    void 사용자가_태스크의_댓글에_대댓글을_작성한다() {
        // given
        User member1 = user.가_로그인을_한다1();
        String member1AccessToken = auth.토큰을_발급한다(member1.getId());
        User member2 = user.가_로그인을_한다2();
        String member2AccessToken = auth.토큰을_발급한다(member2.getId());

        UserProjectResponse createdProject = project.를_생성한다(member1AccessToken);
        task.를_생성한다(member1AccessToken, member1.getId(), createdProject.getId(), null);
        CreateCommentResponse registeredComment = comment.를_등록한다(member2AccessToken, null);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(registeredComment.getId(), "comment contents");

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", member1AccessToken)
                .body(createCommentRequest)

        // when
        .when()
                .post("/api/tasks/" + 1L + "/comments")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("writer.displayName", equalTo(member1.getName()))
                .body("writer.avatar", equalTo(member1.getAvatar()))
                .body("superCommentId", equalTo(registeredComment.getId().intValue()))
                .body("contents", equalTo(createCommentRequest.getContents()));
    }

    @Test
    void 사용자가_태스트의_댓글과_대댓글을_조회한다() {
        // given
        User member1 = user.가_로그인을_한다1();
        String member1AccessToken = auth.토큰을_발급한다(member1.getId());
        User member2 = user.가_로그인을_한다2();
        String member2AccessToken = auth.토큰을_발급한다(member2.getId());

        UserProjectResponse createdProject = project.를_생성한다(member1AccessToken);
        task.를_생성한다(member1AccessToken, member1.getId(), createdProject.getId(), null);

        CreateCommentResponse registeredComment1 = comment.를_등록한다(member2AccessToken, null);
        CreateCommentResponse registeredComment2 = comment.를_등록한다(member2AccessToken, null);
        CreateCommentResponse subComment = comment.를_등록한다(member1AccessToken, registeredComment2.getId());

        List<TaskCommentResponse> response = given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", member1AccessToken)

        // when
        .when()
                .get("/api/tasks/" + 1L + "/comments")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .body()
                .as(new TypeRef<List<TaskCommentResponse>>() {});

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getWriter().getDisplayName()).isEqualTo(member2.getName());
        assertThat(response.get(0).getContents()).isEqualTo(registeredComment1.getContents());
        assertThat(response.get(0).getSubComments().size()).isZero();
        assertThat(response.get(1).getWriter().getDisplayName()).isEqualTo(member2.getName());
        assertThat(response.get(1).getContents()).isEqualTo(registeredComment2.getContents());

        List<TaskCommentResponse> subComments = response.get(1).getSubComments();
        assertThat(subComments.size()).isOne();
        assertThat(subComments.get(0).getWriter().getDisplayName()).isEqualTo(member1.getName());
        assertThat(subComments.get(0).getContents()).isEqualTo(subComment.getContents());
        assertThat(subComments.get(0).getSubComments()).isEmpty();
    }

    @Test
    void 사용자가_자신이_작성한_댓글의_내용을_수정한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), null);
        CreateCommentResponse createdComment = comment.를_등록한다(accessToken, null);

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("new contents");

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(updateCommentRequest)

        // when
        .when()
                .put("/api/tasks/comments/" + createdComment.getId())

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("contents", equalTo(updateCommentRequest.getContents()));
    }

    @Test
    void 사용자는_빈_내용으로_댓글을_수정할_수_없다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), null);
        CreateCommentResponse createdComment = comment.를_등록한다(accessToken, null);

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("");

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(updateCommentRequest)

        // when
        .when()
                .put("/api/tasks/comments/" + createdComment.getId())

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("contents : 공백일 수 없습니다"));
    }

}
