package kr.kro.colla.exception.exception.story;

import kr.kro.colla.exception.exception.NotFoundException;

public class StoryNotFoundException extends NotFoundException {

    public StoryNotFoundException() { super("해당하는 스토리를 찾을 수 없습니다."); }

}
