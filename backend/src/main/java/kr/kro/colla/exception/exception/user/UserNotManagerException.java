package kr.kro.colla.exception.exception.user;

import kr.kro.colla.exception.exception.ForbiddenException;

public class UserNotManagerException extends ForbiddenException {

    public UserNotManagerException() { super("해당 사용자의 권한으로 접근할 수 없습니다.");}
}
