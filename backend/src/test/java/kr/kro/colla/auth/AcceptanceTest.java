package kr.kro.colla.auth;

import io.restassured.RestAssured;
import io.restassured.http.Cookie;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.infrastructure.RedisManager;
import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.auth.service.JwtProvider;
import kr.kro.colla.common.fixture.Auth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @MockBean
    private GithubOAuthManager githubOAuthManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private RedisManager redisManager;

    private Auth auth;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        auth = new Auth(jwtProvider);
    }

    @Test
    void 로그인_성공_시_Jwt토큰이_생성된다() {
        // given
        String oauthCode = "oauth-code";
        String oAuthAccessToken = "oauth-access-token";
        GithubUserProfileResponse githubUserProfileResponse = new GithubUserProfileResponse("name", "github-id", "avatar");

        given(githubOAuthManager.getOAuthAccessToken(oauthCode)).willReturn(oAuthAccessToken);
        given(githubOAuthManager.getUserProfile(oAuthAccessToken)).willReturn(githubUserProfileResponse);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)

        // when
        .when()
                .get("/api/auth/login?code=" + oauthCode)

        // then
        .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .cookie("accessToken", notNullValue())
                .extract();

        String id = jwtProvider.findIdFromToken(response.cookie("accessToken"))
                        .toString();
        assertThat(redisManager.findValue(id)).isNotNull();
    }

    @Test
    void 올바르지_않은_OAuth코드가_올_경우_로그인에_실패한다() {
        // given
        String unauthorizedCode = "unauthorized-code";

        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)

        // when
        .when()
                .get("/api/auth/login?code=" + unauthorizedCode)

        // then
        .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void 로그아웃_시_Jwt토큰을_삭제한다() {
        // given
        Long id = 1L;
        String accessToken = auth.로그인(id);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .cookie("accessToken", accessToken)
        // when
        .when()
                .post("/api/auth/logout")

        // then
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();

        Cookie cookie = response.detailedCookie("accessToken");
        assertThat(cookie.getValue()).isBlank();
        assertThat(cookie.getMaxAge()).isZero();
        assertThat(redisManager.findValue(id.toString())).isNull();
    }

}
