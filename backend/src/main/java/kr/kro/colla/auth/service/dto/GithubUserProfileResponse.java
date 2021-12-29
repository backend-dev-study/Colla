package kr.kro.colla.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GithubUserProfileResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("login")
    private String githubId;

    @JsonProperty("avatar_url")
    private String avatar;

}
