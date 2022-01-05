package kr.kro.colla.auth.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginUser {

    private String githubId;

    @Builder
    public LoginUser(String githubId) {
        this.githubId = githubId;
    }

}
