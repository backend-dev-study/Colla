package kr.kro.colla.common.fixture;

import kr.kro.colla.auth.service.JwtProvider;

public class Auth {

    private JwtProvider jwtProvider;

    public Auth(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public String 로그인(Long id) {
        return jwtProvider.createAccessToken(id);
    }

}
