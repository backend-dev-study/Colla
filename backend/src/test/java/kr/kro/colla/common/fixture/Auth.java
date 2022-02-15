package kr.kro.colla.common.fixture;

import kr.kro.colla.auth.service.JwtProvider;

public class Auth {

    private JwtProvider jwtProvider;

    public Auth(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public String 토큰을_발급한다(Long id) {
        return jwtProvider.createAccessToken(id);
    }

}
