package kr.kro.colla.task.task_status_log.domain.repository;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusLogRepository extends JpaRepository<TaskStatusLog, Long> {

    Optional<TaskStatusLog> findTaskStatusLogByProjectAndTaskAndStatus(Project project, Task task, String status);

}
