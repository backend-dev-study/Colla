package kr.kro.colla.auth.service;

import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.infrastructure.RedisManager;
import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.auth.service.dto.CreateTokenResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final GithubOAuthManager githubOAuthManager;
    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final RedisManager redisManager;

    public String githubLogin(String code) {
        String oAuthAccessToken = this.githubOAuthManager.getOAuthAccessToken(code);
        GithubUserProfileResponse userProfile = this.githubOAuthManager.getUserProfile(oAuthAccessToken);

        User user = this.userService.createUserIfNotExist(userProfile);
        CreateTokenResponse createTokenResponse = this.jwtProvider.createTokens(user.getId());
        this.redisManager.saveRefreshToken(createTokenResponse);

        return createTokenResponse.getAccessToken();
    }

}
