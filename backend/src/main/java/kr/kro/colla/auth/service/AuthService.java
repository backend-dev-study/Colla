package kr.kro.colla.auth.service;

import kr.kro.colla.auth.service.dto.GithubAccessTokenRequest;
import kr.kro.colla.auth.service.dto.GithubAccessTokenResponse;
import kr.kro.colla.auth.service.dto.GithubUserProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Value("${github.client_id}")
    private String clientId;

    @Value("${github.client_secret}")
    private String clientSecret;

    @Value("${github.access_token_url}")
    private String accessTokenUrl;

    @Value("${github.user_profile_url}")
    private String userProfileUrl;

    public GithubUserProfileResponse githubLogin(String code) {
        String accessToken = getAccessToken(code);
        GithubUserProfileResponse userProfile = getUserProfile(accessToken);

        return userProfile;
    }

    public String getAccessToken(String code) {
        GithubAccessTokenRequest githubAccessTokenRequest = new GithubAccessTokenRequest(clientId, clientSecret, code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(githubAccessTokenRequest, headers);

        GithubAccessTokenResponse response = new RestTemplate().exchange(
                accessTokenUrl,
                HttpMethod.POST,
                httpEntity,
                GithubAccessTokenResponse.class
        ).getBody();

        return response.getAccessToken();
    }

    public GithubUserProfileResponse getUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

        GithubUserProfileResponse response = new RestTemplate().exchange(
                userProfileUrl,
                HttpMethod.GET,
                httpEntity,
                GithubUserProfileResponse.class
        ).getBody();

        return response;
    }

}
