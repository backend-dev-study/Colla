package kr.kro.colla.exception.exception.user;

import kr.kro.colla.exception.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("해당하는 사용자를 찾을 수 없습니다.");
    }
}
