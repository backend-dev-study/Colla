package kr.kro.colla.auth.service;

import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.service.dto.GithubUserProfileResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class AuthService {

    private final GithubOAuthManager githubOAuthManager;
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public String githubLogin(String code) {
        try {
            String oAuthAccessToken = this.githubOAuthManager.getOAuthAccessToken(code);
            GithubUserProfileResponse userProfile = this.githubOAuthManager.getUserProfile(oAuthAccessToken);

            createUser(userProfile);
            String jwtAccessToken = this.jwtProvider.createToken(userProfile.getGithubId());

            return jwtAccessToken;
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
