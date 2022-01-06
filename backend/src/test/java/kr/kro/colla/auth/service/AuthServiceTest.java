package kr.kro.colla.auth.service;

import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.infrastructure.RedisManager;
import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.auth.service.dto.CreateTokenResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private GithubOAuthManager githubOAuthManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserService userService;

    @Mock
    private RedisManager redisManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void 로그인_성공_시_Jwt토큰을_반환한다() {
        // given
        String oauthCode = "oauth-code";
        String oAuthAccessToken = "oauth-access-token";
        String jwtAccessToken = "jwt-access-token";
        String jwtRefreshToken = "jwt-refresh-token";
        User user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        GithubUserProfileResponse githubUserProfileResponse = new GithubUserProfileResponse("user", "github-id", "avatar");
        CreateTokenResponse createTokenResponse = new CreateTokenResponse(jwtAccessToken, jwtRefreshToken);

        given(githubOAuthManager.getOAuthAccessToken(oauthCode))
                .willReturn(oAuthAccessToken);
        given(githubOAuthManager.getUserProfile(oAuthAccessToken))
                .willReturn(githubUserProfileResponse);
        given(userService.createUserIfNotExist(githubUserProfileResponse))
                .willReturn(user);
        given(jwtProvider.createTokens(user.getId()))
                .willReturn(createTokenResponse);

        // when
        String accessToken = authService.githubLogin(oauthCode);

        // then
        assertThat(accessToken).isEqualTo(jwtAccessToken);
    }

    @Test
    void 로그인_실패_시_Jwt토큰을_반환하지_않는다() {
        // given
        String unauthorizedCode = "unauthorized-code";

        given(githubOAuthManager.getOAuthAccessToken(unauthorizedCode))
                .willReturn(null);
        given(githubOAuthManager.getUserProfile(null))
                .willThrow(HttpClientErrorException.class);

        // when, then
        assertThatThrownBy(() -> authService.githubLogin(unauthorizedCode))
                .isInstanceOf(HttpClientErrorException.class);
    }

}
