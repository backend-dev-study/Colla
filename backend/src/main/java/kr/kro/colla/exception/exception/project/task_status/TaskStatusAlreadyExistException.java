package kr.kro.colla.exception.exception.project.task_status;

import kr.kro.colla.exception.exception.BadRequestException;

public class TaskStatusAlreadyExistException extends BadRequestException {

    public TaskStatusAlreadyExistException() {
        super("해당 이름의 태스크 상태가 이미 존재합니다.");
    }

}
