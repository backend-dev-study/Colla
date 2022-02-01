package kr.kro.colla.task.task_tag.domain.repository;

import kr.kro.colla.task.task_tag.domain.TaskTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTagRepository extends JpaRepository<TaskTag, Long> {
}
