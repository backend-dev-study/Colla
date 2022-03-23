package kr.kro.colla.common.fixture;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;

public class TaskStatusLogProvider {

    public static TaskStatusLog 태스크_상태_로그_생성(Project project, Task task, TaskStatus taskStatus) {
        return TaskStatusLog.builder()
                .status(taskStatus.getName())
                .project(project)
                .task(task)
                .build();
    }

}
