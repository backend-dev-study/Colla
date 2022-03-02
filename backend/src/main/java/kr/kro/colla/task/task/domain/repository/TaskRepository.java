package kr.kro.colla.task.task.domain.repository;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskSimpleResponse;
import kr.kro.colla.user.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Task t set t.taskStatus = :to where t.taskStatus = :from")
    void bulkUpdateTaskStatusToAnother(@Param("from") TaskStatus from, @Param("to")TaskStatus to);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project order by t.createdAt asc")
    List<Task> findAllOrderByCreatedAtAsc(@Param("project") Project project);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project order by t.createdAt desc")
    List<Task> findAllOrderByCreatedAtDesc(@Param("project") Project project);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project order by t.priority asc")
    List<Task> findAllOrderByPriorityAsc(@Param("project") Project project);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project order by t.priority desc")
    List<Task> findAllOrderByPriorityDesc(@Param("project") Project project);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project and t.taskStatus = :taskStatus")
    List<Task> findAllFilterByTaskStatus(@Param("project") Project project, @Param("taskStatus") TaskStatus taskStatus);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project and ((:managerId is null and t.managerId is null) or t.managerId = :managerId)")
    List<Task> findAllFilterByManager(@Param("project") Project project, @Param("managerId") Long managerId);
}
