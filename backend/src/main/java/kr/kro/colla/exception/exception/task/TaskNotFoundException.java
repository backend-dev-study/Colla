package kr.kro.colla.exception.exception.task;

import kr.kro.colla.exception.exception.NotFoundException;

public class TaskNotFoundException extends NotFoundException {

    public TaskNotFoundException() {
        super("해당하는 태스크를 찾을 수 없습니다.");
    }
}
