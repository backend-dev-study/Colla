package kr.kro.colla.task.task.service;

import kr.kro.colla.exception.exception.task.TaskNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.service.TaskStatusService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.service.StoryService;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import kr.kro.colla.task.task.presentation.dto.CreateTaskRequest;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskResponse;
import kr.kro.colla.task.task.presentation.dto.UpdateTaskRequest;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import kr.kro.colla.task.task_tag.service.TaskTagService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskService {

    private final UserService userService;
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

    public ProjectTaskResponse getTask(Long taskId) {
        Task task = findTaskById(taskId);
        User user = task.getManagerId() != null
                ? userService.findUserById(task.getManagerId())
                : null;

        return ProjectTaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .story(task.getStory() != null ? task.getStory().getTitle() : null)
                .preTasks(task.getPreTasks())
                .manager(user != null ? user.getName() : null)
                .status(task.getTaskStatus().getName())
                .priority(task.getPriority())
                .tags(task.getTaskTags()
                        .stream()
                        .map(taskTag -> taskTag.getTag().getName())
                        .collect(Collectors.toList()))
                .build();
    }

    public void updateTask(Long taskId, UpdateTaskRequest updateTaskRequest) {
        Task task = findTaskById(taskId);
        String title = task.getStory().getTitle();
        List<TaskTag> taskTags = taskTagService.translateTaskTags(task, updateTaskRequest.getTags());

        task.updateContents(updateTaskRequest);
        task.updateTags(taskTags);

        if (!title.equals(updateTaskRequest.getStory())) {
            Story story = storyService.findStoryByTitle(updateTaskRequest.getStory());
            task.updateStory(story);
        }
    }

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
    }

}
