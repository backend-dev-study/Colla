package kr.kro.colla.auth.presentation.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GithubLoginResponse {

    private String accessToken;

    private String message;

    @Builder
    public GithubLoginResponse(String accessToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
    }

}
