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
import kr.kro.colla.config.query_counter.QueryCountConfig;
import kr.kro.colla.config.query_counter.Counter;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;

@Import(QueryCountConfig.class)
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

    @Autowired
    private Counter counter;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        auth = new Auth(jwtProvider);
        databaseCleaner.execute();
    }

    @Test
    void ????????????_????????????_?????????_????????????() {
        // given
        User registeredUser = user.???_????????????_??????1();
        String accessToken = auth.?????????_????????????(registeredUser.getId());
        UserProjectResponse createdProject = project.???_????????????(accessToken);
        task.???_????????????(accessToken, registeredUser.getId(), createdProject.getId(), null);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(null, "comment contents");

        counter.startQueryCount();

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

        counter.printQueryCount();
    }

    @Test
    void ????????????_????????????_?????????_????????????_????????????() {
        // given
        User member1 = user.???_????????????_??????1();
        String member1AccessToken = auth.?????????_????????????(member1.getId());
        User member2 = user.???_????????????_??????2();
        String member2AccessToken = auth.?????????_????????????(member2.getId());

        UserProjectResponse createdProject = project.???_????????????(member1AccessToken);
        task.???_????????????(member1AccessToken, member1.getId(), createdProject.getId(), null);
        CreateCommentResponse registeredComment = comment.???_????????????(member2AccessToken, null);

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
    void ????????????_????????????_?????????_????????????_????????????() {
        // given
        User member1 = user.???_????????????_??????1();
        String member1AccessToken = auth.?????????_????????????(member1.getId());
        User member2 = user.???_????????????_??????2();
        String member2AccessToken = auth.?????????_????????????(member2.getId());

        UserProjectResponse createdProject = project.???_????????????(member1AccessToken);
        task.???_????????????(member1AccessToken, member1.getId(), createdProject.getId(), null);

        CreateCommentResponse registeredComment1 = comment.???_????????????(member2AccessToken, null);
        CreateCommentResponse registeredComment2 = comment.???_????????????(member2AccessToken, null);
        CreateCommentResponse subComment = comment.???_????????????(member1AccessToken, registeredComment2.getId());

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
    void ????????????_?????????_?????????_?????????_?????????_????????????() {
        // given
        User registeredUser = user.???_????????????_??????1();
        String accessToken = auth.?????????_????????????(registeredUser.getId());
        UserProjectResponse createdProject = project.???_????????????(accessToken);
        task.???_????????????(accessToken, registeredUser.getId(), createdProject.getId(), null);
        CreateCommentResponse createdComment = comment.???_????????????(accessToken, null);

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("new contents");

        counter.startQueryCount();

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

        counter.printQueryCount();
    }

    @Test
    void ????????????_???_????????????_?????????_?????????_???_??????() {
        // given
        User registeredUser = user.???_????????????_??????1();
        String accessToken = auth.?????????_????????????(registeredUser.getId());
        UserProjectResponse createdProject = project.???_????????????(accessToken);
        task.???_????????????(accessToken, registeredUser.getId(), createdProject.getId(), null);
        CreateCommentResponse createdComment = comment.???_????????????(accessToken, null);

        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("");

        given()
                .contentType(ContentType.JSON)
                .header("Accept-Language", "ko")
                .cookie("accessToken", accessToken)
                .body(updateCommentRequest)

        // when
        .when()
                .put("/api/tasks/comments/" + createdComment.getId())

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", equalTo("contents : ????????? ??? ????????????"));
    }

}
