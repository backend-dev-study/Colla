package kr.kro.colla.auth.service;

import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.service.dto.GithubUserProfileResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private GithubOAuthManager githubOAuthManager;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void 로그인_성공_시_Jwt토큰을_반환한다() {
        // given
        String oauthCode = "oauth-code";
        String oAuthAccessToken = "oauth-access-token";
        String jwtAccessToken = "jwt-access-token";
        GithubUserProfileResponse githubUserProfileResponse = new GithubUserProfileResponse("user", "github-id", "avatar");

        given(githubOAuthManager.getOAuthAccessToken(oauthCode)).willReturn(oAuthAccessToken);
        given(githubOAuthManager.getUserProfile(oAuthAccessToken)).willReturn(githubUserProfileResponse);
        given(jwtProvider.createToken(githubUserProfileResponse.getGithubId())).willReturn(jwtAccessToken);

        // when
        String accessToken = authService.githubLogin(oauthCode);

        // then
        assertThat(accessToken).isEqualTo(jwtAccessToken);
    }

}
