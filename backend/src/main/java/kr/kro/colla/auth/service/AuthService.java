package kr.kro.colla.auth.service;

import kr.kro.colla.auth.infrastructure.GithubOAuthManager;
import kr.kro.colla.auth.infrastructure.RedisManager;
import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.auth.service.dto.CreateTokenResponse;
import kr.kro.colla.exception.exception.auth.InvalidTokenException;
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
        this.redisManager.saveRefreshToken(user.getId(), createTokenResponse.getRefreshToken());

        return createTokenResponse.getAccessToken();
    }

    public boolean validateAccessToken(String accessToken) {
        return this.jwtProvider.validateToken(accessToken);
    }

    public String validateRefreshToken(String accessToken) {
        Long id = this.jwtProvider.findIdFromToken(accessToken);
        String refreshToken = this.redisManager.findValue(id.toString());
        boolean isValid = this.jwtProvider.validateToken(refreshToken);

        if(!isValid) {
            throw new InvalidTokenException();
        }

        return this.jwtProvider.createAccessToken(id);
    }

    public Long extractIdFromToken(String token) {
        return this.jwtProvider.findIdFromToken(token);
    }

}
