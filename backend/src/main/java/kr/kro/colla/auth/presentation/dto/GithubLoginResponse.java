package kr.kro.colla.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubLoginResponse {

    private String name;

    private String avatar;

    private String accessToken;

}
