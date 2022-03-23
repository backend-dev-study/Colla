package kr.kro.colla.task.task_status_log.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import kr.kro.colla.task.task_status_log.domain.repository.TaskStatusLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskStatusLogService {

    private final String DONE = "Done";
    private final TaskStatusLogRepository taskStatusLogRepository;

    public void writeTaskStatusLog(Project project, Task task, TaskStatus prevStatus, TaskStatus newStatus) {
        if (prevStatus == null || newStatus.getName().equalsIgnoreCase(DONE)) {
            TaskStatusLog taskStatusLog = TaskStatusLog.builder()
                    .status(newStatus.getName())
                    .project(project)
                    .task(task)
                    .build();
            taskStatusLogRepository.save(taskStatusLog);
            return;
        }

        if (prevStatus.getName().equalsIgnoreCase(DONE)) {
            taskStatusLogRepository.findTaskStatusLogByProjectAndTaskAndStatus(project, task, DONE)
                    .ifPresent(taskStatusLogRepository::delete);
        }
    }

}
