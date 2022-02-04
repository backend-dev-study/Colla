package kr.kro.colla.exception.exception.auth;

import kr.kro.colla.exception.exception.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {

    public InvalidTokenException() {
        super("유효하지 않은 토큰입니다.");
    }
}
