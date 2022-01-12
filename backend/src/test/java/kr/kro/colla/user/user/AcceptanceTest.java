package kr.kro.colla.user.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.fixture.Auth;
import kr.kro.colla.exception.exception.user.UserNotFoundException;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;

import kr.kro.colla.user.user.presentation.dto.UpdateUserNameRequest;
import kr.kro.colla.user.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private Auth auth;
    private User user;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        auth = new Auth(jwtProvider);

        user = User.builder()
                .githubId("binimini")
                .name("subin")
                .avatar("github_content")
                .build();
        userRepository.save(user);
    }

    @Test
    void 로그인한_사용자의_프로필을_조회한다() {
        // given
        String accessToken = auth.로그인(user.getId());

        given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
        // when
        .when()
                .get("/api/users/profile")
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("displayName", equalTo(user.getName()))
                .body("githubId", equalTo(user.getGithubId()))
                .body("avatar", equalTo(user.getAvatar()));
    }

    @Test
    void 사용자_프로젝트_생성_성공_후_반환한다() {
        // given
        String accessToken = auth.로그인(user.getId());
        String name = "프로젝트 이름", desc = "프로젝트 설명";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("description",desc);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(requestBody)
        // when
        .when()
                .post("/api/users/{userId}/projects", user.getId())
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", notNullValue())
                .body("managerId", equalTo(user.getId().intValue()))
                .body("name", equalTo(name))
                .body("description", equalTo(desc));
    }

    @Test
    void 사용자_프로젝트_생성_시_요청이_잘못돼_에러를_반환한다() throws Exception {
        // given
        String accessToken = auth.로그인(user.getId());
        String name = "프로젝트 이름", desc = "프로젝트 설명";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("description", desc);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(requestBody)
        // when
        .when()
                .post("/api/users/{userId}/projects", user.getId())

        // then
        .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("status", equalTo(400))
                .body("message", equalTo("name : 공백일 수 없습니다"));
    }

    @Test
    void 사용자_프로젝트_생성_시_없는_사용자_아이디_요청에_에러_반환한다(){
        // given
        String accessToken = auth.로그인(user.getId());
        String name = "프로젝트 이름", desc = "프로젝트 설명";
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("description",desc);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(requestBody)
        // when
        .when()
                .post("/api/users/{userId}/projects",123123)

        // then
        .then().log().all()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .body("status", equalTo(404))
            .body("message", equalTo(new UserNotFoundException().getMessage()));
    }

    @Test
    void 사용자는_이름을_변경할_수_있다() {
        // given
        String accessToken = auth.로그인(user.getId());
        String newDisplayName = "new-name";
        UpdateUserNameRequest updateUserNameRequest = new UpdateUserNameRequest(newDisplayName);

        given()
                .contentType(ContentType.JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
                .body(updateUserNameRequest)
        // when
        .when()
                .patch("/api/users/name")
        // then
        .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo(newDisplayName));
    }
}
