package kr.kro.colla.task.task_status_log.domain.repository;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskStatusLogRepository extends JpaRepository<TaskStatusLog, Long> {

    Optional<TaskStatusLog> findTaskStatusLogByProjectAndStatusAndCreatedAt(Project project, String status, LocalDate createdAt);

    List<TaskStatusLog> findTaskStatusLogsByProjectAndCreatedAtBetween(Project project, LocalDate start, LocalDate end);

}
