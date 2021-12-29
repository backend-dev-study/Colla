package kr.kro.colla.auth.infrastructure;

import kr.kro.colla.auth.service.dto.GithubAccessTokenRequest;
import kr.kro.colla.auth.service.dto.GithubAccessTokenResponse;
import kr.kro.colla.auth.service.dto.GithubUserProfileResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubOAuthManager {

    @Value("${github.client_id}")
    private String clientId;

    @Value("${github.client_secret}")
    private String clientSecret;

    @Value("${github.access_token_url}")
    private String accessTokenUrl;

    @Value("${github.user_profile_url}")
    private String userProfileUrl;

    public String getOAuthAccessToken(String code) {
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
