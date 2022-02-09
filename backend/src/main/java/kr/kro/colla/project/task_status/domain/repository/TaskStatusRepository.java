package kr.kro.colla.project.task_status.domain.repository;

import kr.kro.colla.project.task_status.domain.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

    Optional<TaskStatus> findByName(String name);

}
