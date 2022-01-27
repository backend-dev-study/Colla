package kr.kro.colla.exception.exception.notice;

import kr.kro.colla.exception.exception.NotFoundException;

public class NoticeNotFoundException extends NotFoundException {
    public NoticeNotFoundException() { super("해당하는 알림를 찾을 수 없습니다."); }
}
