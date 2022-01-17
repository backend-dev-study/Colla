package kr.kro.colla.exception.exception.project;

import kr.kro.colla.exception.exception.NotFoundException;

public class ProjectNotFoundException extends NotFoundException {

    public ProjectNotFoundException() { super("해당하는 프로젝트를 찾을 수 없습니다."); }
}
