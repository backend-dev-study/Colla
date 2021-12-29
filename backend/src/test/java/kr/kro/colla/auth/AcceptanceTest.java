package kr.kro.colla.auth;

import io.restassured.RestAssured;
import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.service.dto.GithubUserProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @MockBean
    private GithubOAuthManager githubOAuthManager;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 로그인_성공_시_Jwt토큰이_반환된다() {
        // given
        String oauthCode = "oauth-code";
        String oAuthAccessToken = "oauth-access-token";
        GithubUserProfileResponse githubUserProfileResponse = new GithubUserProfileResponse("name", "github-id", "avatar");

        given(githubOAuthManager.getOAuthAccessToken(oauthCode)).willReturn(oAuthAccessToken);
        given(githubOAuthManager.getUserProfile(oAuthAccessToken)).willReturn(githubUserProfileResponse);

        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)

        // when
        .when()
                .get("/api/auth/login?code=" + oauthCode)

        // then
        .then().log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", notNullValue());
    }

}
