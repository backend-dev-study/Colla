package kr.kro.colla.task.task_status_log.domain.repository;

import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TaskStatusLogRepository extends JpaRepository<TaskStatusLog, Long> {

    Optional<TaskStatusLog> findTaskStatusLogByStatusAndCreatedAt(String status, LocalDate createdAt);

}
