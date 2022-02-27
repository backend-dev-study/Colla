package kr.kro.colla.exception.exception.user;

import kr.kro.colla.exception.exception.ForbiddenException;

public class UserNotReceiverException extends ForbiddenException {

    public UserNotReceiverException() { super("해당 사용자가 접근하는 알림의 수신자가 아닙니다.");}
}
