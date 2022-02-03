package kr.kro.colla.task.task.domain.repository;

import kr.kro.colla.task.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
