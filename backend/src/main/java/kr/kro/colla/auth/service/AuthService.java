package kr.kro.colla.auth.service;

import kr.kro.colla.auth.domain.LoginUser;
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
        String oAuthAccessToken = githubOAuthManager.getOAuthAccessToken(code);
        GithubUserProfileResponse userProfile = githubOAuthManager.getUserProfile(oAuthAccessToken);

        User user = userService.createOrUpdateUser(userProfile);
        CreateTokenResponse createTokenResponse = jwtProvider.createTokens(user.getId());
        redisManager.saveRefreshToken(user.getId(), createTokenResponse.getRefreshToken());

        return createTokenResponse.getAccessToken();
    }

    public boolean validateAccessToken(String accessToken) {
        return jwtProvider.validateToken(accessToken);
    }

    public String validateRefreshToken(String accessToken) {
        Long id = jwtProvider.findIdFromToken(accessToken);
        String refreshToken = redisManager.findValue(id.toString());
        boolean isValid = jwtProvider.validateToken(refreshToken);

        if(!isValid) {
            throw new InvalidTokenException();
        }

        return jwtProvider.createAccessToken(id);
    }

    public LoginUser findUserFromToken(String token) {
        Long id = jwtProvider.findIdFromToken(token);
        return new LoginUser(id);
    }

    public void removeRefreshToken(Long id) {
        redisManager.removeRefreshToken(id);
    }

}
