package kr.kro.colla.auth.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateTokenResponse {

    private String accessToken;

    private String refreshToken;

}
