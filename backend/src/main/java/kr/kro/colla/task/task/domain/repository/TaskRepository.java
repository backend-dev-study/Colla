package kr.kro.colla.task.task.domain.repository;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.dto.TaskCountByStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying(clearAutomatically = true)
    @Query("update Task t set t.taskStatus = :to where t.taskStatus = :from")
    int bulkUpdateTaskStatusToAnother(@Param("from") TaskStatus from, @Param("to")TaskStatus to);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.story = :story")
    List<Task> findStoryTasks(@Param("story") Story story);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project order by t.createdAt asc")
    List<Task> findAllOrderByCreatedAtAsc(@Param("project") Project project);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project order by t.createdAt desc")
    List<Task> findAllOrderByCreatedAtDesc(@Param("project") Project project);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project order by t.priority asc")
    List<Task> findAllOrderByPriorityAsc(@Param("project") Project project);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project order by t.priority desc")
    List<Task> findAllOrderByPriorityDesc(@Param("project") Project project);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project and t.taskStatus.name in :taskStatuses")
    List<Task> findAllFilterByTaskStatus(@Param("project") Project project, @Param("taskStatuses") List<String> taskStatuses);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project and ((:canNull = true and t.managerId is null) or t.managerId in :managers)")
    List<Task> findAllFilterByManager(@Param("project") Project project, @Param("managers") List<Long> managers, @Param("canNull") Boolean canNull);

    @Query("select distinct t from Task t left join fetch t.taskTags tt left join fetch tt.tag where t.project = :project and t.title like %:keyword%")
    List<Task> findTasksSearchByKeyword(@Param("project") Project project, @Param("keyword") String keyword);

    @Query("select new kr.kro.colla.task.task.domain.dto.TaskCountByStatus(t.taskStatus.name, count(t)) from Task t where t.project = :project group by t.taskStatus")
    List<TaskCountByStatus> groupByTaskStatus(@Param("project") Project project);

    @Query("select new kr.kro.colla.task.task.domain.dto.TaskCountByStatus(t.taskStatus.name, count(t), u.name) from Task t left join fetch User u on u.id = t.managerId where t.project = :project group by t.taskStatus, t.managerId")
    List<TaskCountByStatus> groupByTaskStatusAndManager(@Param("project") Project project);
}
