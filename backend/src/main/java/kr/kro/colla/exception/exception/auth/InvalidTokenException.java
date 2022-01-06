package kr.kro.colla.exception.exception.auth;

public class InvalidTokenException extends UnauthorizedException {

    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }
}
