package kr.kro.colla.common.fixture;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskProvider {

    @Autowired
    private TaskRepository taskRepository;

    public Task 를_생성한다(Long managerId, Project project, Story story) {
        Task task = Task.builder()
                .title("task title")
                .managerId(managerId)
                .description("task description")
                .priority(4)
                .project(project)
                .taskStatus(project.getTaskStatuses().get(0))
                .story(story)
                .preTasks("[]")
                .build();

        return taskRepository.save(task);
    }

    public static Task createTask(Long managerId, Project project, Story story) {
        return Task.builder()
                .title("task title")
                .managerId(managerId)
                .description("task description")
                .priority(4)
                .project(project)
                .taskStatus(new TaskStatus("To Do"))
                .story(story)
                .preTasks("[]")
                .build();
    }

}
