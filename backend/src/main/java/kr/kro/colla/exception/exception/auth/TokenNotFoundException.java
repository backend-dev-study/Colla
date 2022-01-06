package kr.kro.colla.exception.exception.auth;

public class TokenNotFoundException extends UnauthorizedException {

    public TokenNotFoundException() {
        super("토큰이 존재하지 않습니다.");
    }
}
