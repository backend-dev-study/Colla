package kr.kro.colla.task.task.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.service.TaskStatusService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.service.StoryService;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import kr.kro.colla.task.task.presentation.dto.CreateTaskRequest;
import kr.kro.colla.task.task_tag.service.TaskTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskService {

    private final StoryService storyService;
    private final ProjectService projectService;
    private final TaskTagService taskTagService;
    private final TaskStatusService taskStatusService;
    private final TaskRepository taskRepository;

    public Long createTask(CreateTaskRequest createTaskRequest) {
        Project project = projectService.findProjectById(createTaskRequest.getProjectId());
        Story story = createTaskRequest.getStory() != null
                ? storyService.findStoryByTitle(createTaskRequest.getStory())
                : null;
        TaskStatus taskStatus = taskStatusService.findTaskStatusByName(createTaskRequest.getStatus());

        Task task = Task.builder()
                .title(createTaskRequest.getTitle())
                .managerId(createTaskRequest.getManagerId())
                .description(createTaskRequest.getDescription())
                .priority(createTaskRequest.getPriority())
                .project(project)
                .taskStatus(taskStatus)
                .story(story)
                .preTasks(createTaskRequest.getPreTasks())
                .build();

        if (createTaskRequest.getTags() != null) {
            taskTagService.setTaskTag(task, createTaskRequest.getTags());
        }

        return taskRepository.save(task)
                .getId();
    }

}
