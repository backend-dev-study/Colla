package kr.kro.colla.auth.service;

import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.service.dto.GithubUserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final GithubOAuthManager githubOAuthManager;
    private final JwtProvider jwtProvider;

    public String githubLogin(String code) {
        String oAuthAccessToken = this.githubOAuthManager.getOAuthAccessToken(code);
        GithubUserProfileResponse userProfile = this.githubOAuthManager.getUserProfile(oAuthAccessToken);

        String jwtAccessToken = this.jwtProvider.createToken(userProfile.getGithubId());

        return jwtAccessToken;
    }

}
