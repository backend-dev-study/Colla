package kr.kro.colla.task.task.domain.repository;

import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Task t SET t.taskStatus = :to WHERE  t.taskStatus = :from")
    void bulkUpdateTaskStatusToAnother(@Param("from") TaskStatus from, @Param("to")TaskStatus to);
}
