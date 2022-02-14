package kr.kro.colla.comment;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.comment.presentation.dto.CreateCommentRequest;
import kr.kro.colla.comment.presentation.dto.CreateCommentResponse;
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


import static io.restassured.RestAssured.given;
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

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(1L, null, "comment contents");

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(createCommentRequest)

        // when
        .when()
                .post("/api/task/comments")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("userId", equalTo(registeredUser.getId().intValue()))
                .body("superCommentId", nullValue())
                .body("contents", equalTo(createCommentRequest.getContents()));
    }

    @Test
    void 사용자가_태스크의_댓글에_대댓글을_작성한다() {
        // given
        User registeredUser = user.가_로그인을_한다1();
        String accessToken = auth.토큰을_발급한다(registeredUser.getId());
        UserProjectResponse createdProject = project.를_생성한다(accessToken);
        task.를_생성한다(accessToken, registeredUser.getId(), createdProject.getId(), null);
        CreateCommentResponse registeredComment = comment.를_등록한다(accessToken);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(1L, registeredComment.getId(), "comment contents");

        given()
                .contentType(ContentType.JSON)
                .cookie("accessToken", accessToken)
                .body(createCommentRequest)

        // when
        .when()
                .post("/api/task/comments")

        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("userId", equalTo(registeredUser.getId().intValue()))
                .body("superCommentId", equalTo(registeredComment.getId().intValue()))
                .body("contents", equalTo(createCommentRequest.getContents()));
    }

}
