package kr.kro.colla.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GithubUserProfileResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("login")
    private String githubId;

    @JsonProperty("avatar_url")
    private String avatar;

}
