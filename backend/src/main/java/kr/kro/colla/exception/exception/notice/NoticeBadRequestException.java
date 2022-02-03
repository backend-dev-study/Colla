package kr.kro.colla.exception.exception.notice;

import kr.kro.colla.exception.exception.BadRequestException;

public class NoticeBadRequestException extends BadRequestException {

    public NoticeBadRequestException() { super("알림을 생성할 수 없는 요청입니다. 요청 형식을 확인해주세요"); }
}
