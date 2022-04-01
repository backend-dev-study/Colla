package kr.kro.colla.task.task_status_log.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import kr.kro.colla.task.task_status_log.domain.repository.TaskStatusLogRepository;
import kr.kro.colla.task.task_status_log.presentation.dto.TaskStatusLogResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskStatusLogService {

    private final ProjectService projectService;
    private final TaskStatusLogRepository taskStatusLogRepository;

    public void writeTaskStatusLog(Project project, TaskStatus taskStatus) {
        taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(project, taskStatus.getName(), LocalDate.now())
                .ifPresentOrElse(
                        TaskStatusLog::increaseCount,
                        () -> taskStatusLogRepository.save(
                                TaskStatusLog.builder()
                                        .project(project)
                                        .status(taskStatus.getName())
                                        .build()
                        )
                );
    }

    public void updateTaskStatusLog(Project project, Task task, TaskStatus prevStatus, TaskStatus newStatus) {
        if (task.getUpdatedAt().toLocalDate().isEqual(LocalDate.now())) {
            taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(project, prevStatus.getName(), LocalDate.now())
                    .ifPresent(TaskStatusLog::decreaseCount);
        }
        writeTaskStatusLog(project, newStatus);
    }

    public void updateTaskStatusLogForTaskStatusDeletion(Project project, TaskStatus taskStatus, int count) {
        taskStatusLogRepository.findTaskStatusLogByProjectAndStatusAndCreatedAt(project, taskStatus.getName(), LocalDate.now())
                .ifPresentOrElse(
                        taskStatusLog -> IntStream.range(0, count)
                                .forEach(i -> taskStatusLog.increaseCount()),
                        () -> {
                            TaskStatusLog taskStatusLog = TaskStatusLog.builder()
                                    .project(project)
                                    .status(taskStatus.getName())
                                    .build();
                            IntStream.range(0, count - 1)
                                    .forEach(i -> taskStatusLog.increaseCount());
                            taskStatusLogRepository.save(taskStatusLog);
                        }
                );
    }

    public Map<String, List<TaskStatusLogResponse>> getWeeklyTaskStatusLog(Long projectId) {
        LocalDate end = LocalDate.now();
        LocalDate start = end.minusDays(6);
        Project project = projectService.findProjectById(projectId);
        Map<String, List<TaskStatusLogResponse>> map = new HashMap<>();

        for (TaskStatus taskStatus : project.getTaskStatuses()) {
            map.put(taskStatus.getName(), new ArrayList<>());
        }

        taskStatusLogRepository.findTaskStatusLogsByProjectAndCreatedAtBetween(project, start, end)
                .forEach(taskStatusLog -> map.get(taskStatusLog.getStatus()).add(new TaskStatusLogResponse(taskStatusLog)));

        return map;
    }

}
