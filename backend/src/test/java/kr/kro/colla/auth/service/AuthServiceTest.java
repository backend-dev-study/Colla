package kr.kro.colla.auth.service;

import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private GithubOAuthManager githubOAuthManager;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void 로그인_성공_시_Jwt토큰을_반환한다() {
        // given
        String oauthCode = "oauth-code";
        String oAuthAccessToken = "oauth-access-token";
        String jwtAccessToken = "jwt-access-token";
        GithubUserProfileResponse githubUserProfileResponse = new GithubUserProfileResponse("user", "github-id", "avatar");

        given(githubOAuthManager.getOAuthAccessToken(oauthCode))
                .willReturn(oAuthAccessToken);
        given(githubOAuthManager.getUserProfile(oAuthAccessToken))
                .willReturn(githubUserProfileResponse);
        given(jwtProvider.createToken(githubUserProfileResponse.getGithubId()))
                .willReturn(jwtAccessToken);

        // when
        String accessToken = authService.githubLogin(oauthCode);

        // then
        assertThat(accessToken).isEqualTo(jwtAccessToken);
    }

    @Test
    void 로그인_실패_시_Jwt토큰을_반환하지_않는다() {
        // given
        String unauthorizedCode = "unauthorized-code";

        // when
        String accessToken = authService.githubLogin(unauthorizedCode);

        // then
        assertThat(accessToken).isNull();
    }

    @Test
    void 첫_로그인_시_회원가입을_진행한다() {
        // given
        String oauthCode = "oauth-code";
        String oAuthAccessToken = "oauth-access-token";
        String jwtAccessToken = "jwt-access-token";
        GithubUserProfileResponse githubUserProfileResponse = new GithubUserProfileResponse("user", "kykapple", "avatar");

        given(githubOAuthManager.getOAuthAccessToken(oauthCode))
                .willReturn(oAuthAccessToken);
        given(githubOAuthManager.getUserProfile(oAuthAccessToken))
                .willReturn(githubUserProfileResponse);
        given(jwtProvider.createToken(githubUserProfileResponse.getGithubId()))
                .willReturn(jwtAccessToken);
        given(userRepository.findByGithubId(githubUserProfileResponse.getGithubId()))
                .willReturn(Optional.empty());

        // when
        String accessToken = authService.githubLogin(oauthCode);

        // then
        assertThat(accessToken).isEqualTo(jwtAccessToken);
        verify(userRepository, times(1)).findByGithubId(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void 기존_사용자일_경우_회원가입을_진행하지_않는다() {
        // given
        String oauthCode = "oauth-code";
        String oAuthAccessToken = "oauth-access-token";
        String jwtAccessToken = "jwt-access-token";
        GithubUserProfileResponse githubUserProfileResponse = new GithubUserProfileResponse("user", "kykapple", "avatar");
        User user = new User("user", "kykapple", "avatar");

        given(githubOAuthManager.getOAuthAccessToken(oauthCode))
                .willReturn(oAuthAccessToken);
        given(githubOAuthManager.getUserProfile(oAuthAccessToken))
                .willReturn(githubUserProfileResponse);
        given(jwtProvider.createToken(githubUserProfileResponse.getGithubId()))
                .willReturn(jwtAccessToken);
        given(userRepository.findByGithubId(githubUserProfileResponse.getGithubId()))
                .willReturn(Optional.of(user));

        // when
        String accessToken = authService.githubLogin(oauthCode);

        // then
        assertThat(accessToken).isEqualTo(jwtAccessToken);
        verify(userRepository, times(1)).findByGithubId(any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
    }

}
