package kr.kro.colla.exception.exception.meeting_place;

import kr.kro.colla.exception.exception.NotFoundException;

public class MeetingPlaceNotFoundException extends NotFoundException {

    public MeetingPlaceNotFoundException() {
        super("해당하는 모임 장소를 찾을 수 없습니다.");
    }
}
