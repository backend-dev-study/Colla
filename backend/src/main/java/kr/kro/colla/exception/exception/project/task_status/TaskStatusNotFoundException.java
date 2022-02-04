package kr.kro.colla.exception.exception.project.task_status;

import kr.kro.colla.exception.exception.NotFoundException;

public class TaskStatusNotFoundException extends NotFoundException {

    public TaskStatusNotFoundException() {
        super("해당하는 태스크 상태가 존재하지 않습니다.");
    }

}
