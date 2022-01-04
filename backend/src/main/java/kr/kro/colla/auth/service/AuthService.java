package kr.kro.colla.auth.service;

import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.infrastructure.RedisManager;
import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.auth.service.dto.CreateTokenResponse;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import kr.kro.colla.user.user.domain.User;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final GithubOAuthManager githubOAuthManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RedisManager redisManager;

    public String githubLogin(String code) {
        try {
            String oAuthAccessToken = this.githubOAuthManager.getOAuthAccessToken(code);
            GithubUserProfileResponse userProfile = this.githubOAuthManager.getUserProfile(oAuthAccessToken);

            createUser(userProfile);
            CreateTokenResponse createTokenResponse = this.jwtProvider.createTokens(userProfile.getGithubId());
            this.redisManager.saveRefreshToken(createTokenResponse);

            return createTokenResponse.getAccessToken();
        } catch(Exception e) {
            return null;
        }
    }

    public void createUser(GithubUserProfileResponse userProfile) {
        this.userRepository.findByGithubId(userProfile.getGithubId())
                .orElseGet(() -> this.userRepository.save(
                        User.builder()
                                .name(userProfile.getName())
                                .githubId(userProfile.getGithubId())
                                .avatar(userProfile.getAvatar())
                                .build()
                    )
                );
    }

}
