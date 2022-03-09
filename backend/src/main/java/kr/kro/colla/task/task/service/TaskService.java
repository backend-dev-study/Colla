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
import kr.kro.colla.task.task.presentation.dto.*;
import kr.kro.colla.task.task.service.converter.TaskResponseConverter;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import kr.kro.colla.task.task_tag.service.TaskTagService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
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
        Story story = !createTaskRequest.getStory().isBlank()
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

        List<TaskTag> tags = taskTagService.translateTaskTags(task, createTaskRequest.getTags());
        task.addTags(tags);

        return taskRepository.save(task)
                .getId();
    }

    public ProjectTaskResponse getTask(Long taskId) {
        Task task = findTaskById(taskId);
        User manager = task.getManagerId() != null
                ? userService.findUserById(task.getManagerId())
                : null;

        return TaskResponseConverter.convertToProjectTaskResponse(task, manager);
    }

    public void updateTask(Long taskId, UpdateTaskRequest updateTaskRequest) {
        Task task = findTaskById(taskId);
        String title = task.getStory() != null ? task.getStory().getTitle() : null;
        List<TaskTag> taskTags = taskTagService.translateTaskTags(task, updateTaskRequest.getTags());

        task.updateContents(updateTaskRequest);
        task.updateTags(taskTags);

        if (title == null && !updateTaskRequest.getStory().isBlank()
                || title != null && !title.equals(updateTaskRequest.getStory())) {
            Story story = storyService.findStoryByTitle(updateTaskRequest.getStory());
            task.updateStory(story);
        }
    }

    public void deleteTaskStatus(Long projectId, String from, String to) {
        TaskStatus fromTaskStatus = taskStatusService.findTaskStatusByName(from);
        TaskStatus toTaskStatus = taskStatusService.findTaskStatusByName(to);
        taskRepository.bulkUpdateTaskStatusToAnother(fromTaskStatus, toTaskStatus);

        Project project = projectService.findProjectById(projectId);
        project.removeStatus(fromTaskStatus);
    }

    public void updateTaskStatus(Long taskId, String statusName) {
        Task task = findTaskById(taskId);
        TaskStatus taskStatus = taskStatusService.findTaskStatusByName(statusName);
        task.updateTaskStatus(taskStatus);
    }

    public List<RoadmapTaskResponse> getStoryTasks(Long projectId, Long storyId) {
        Project project = projectService.findProjectById(projectId);
        Story story = storyService.findStoryById(storyId);
        List<Task> taskList = taskRepository.findStoryTasks(story);
        Hibernate.initialize(project.getMembers());

        return taskList.stream()
                .map(task -> {
                    User manager = task.getManagerId() != null
                            ? userService.findUserById(task.getManagerId())
                            : null;

                    return TaskResponseConverter.convertToRoadmapTaskResponse(task, manager);
                }).collect(Collectors.toList());
    }

    public List<ProjectTaskSimpleResponse> getTasksOrderByCreatedDate(Long projectId, Boolean ascending) {
        Project project = projectService.initializeProjectInfo(projectId);

        List<Task> taskList = ascending
                ? taskRepository.findAllOrderByCreatedAtAsc(project)
                : taskRepository.findAllOrderByCreatedAtDesc(project);

        return taskList.stream()
                .map(task -> {
                    User manager = task.getManagerId() != null
                            ? userService.findUserById(task.getManagerId())
                            : null;

                    return TaskResponseConverter.convertToProjectTaskSimpleResponse(task, manager);
                }).collect(Collectors.toList());
    }

    public List<ProjectTaskSimpleResponse> getTasksOrderByPriority(Long projectId, Boolean ascending) {
        Project project = projectService.initializeProjectInfo(projectId);

        List<Task> taskList = ascending
                ? taskRepository.findAllOrderByPriorityAsc(project)
                : taskRepository.findAllOrderByPriorityDesc(project);

        return taskList.stream()
                .map(task -> {
                    User manager = task.getManagerId() != null
                            ? userService.findUserById(task.getManagerId())
                            : null;

                    return TaskResponseConverter.convertToProjectTaskSimpleResponse(task, manager);
                }).collect(Collectors.toList());
    }

    public List<ProjectTaskSimpleResponse> getTasksFilterByTags(Long projectId, List<String> tags) {
        Project project = projectService.initializeProjectInfo(projectId);
        List<Task> taskList = taskRepository.findAllOrderByCreatedAtDesc(project);

        return taskList.stream()
                .filter(task -> {
                    List<String> taskTags = task.getTaskTags()
                            .stream()
                            .map(taskTag -> taskTag.getTag().getName())
                            .sorted()
                            .collect(Collectors.toList());

                    return taskTags.containsAll(tags);
                }).map(task -> {
                    User manager = task.getManagerId() != null
                            ? userService.findUserById(task.getManagerId())
                            : null;

                    return TaskResponseConverter.convertToProjectTaskSimpleResponse(task, manager);
                }).collect(Collectors.toList());
    }

    public List<ProjectTaskSimpleResponse> getTasksFilterByStatus(Long projectId, List<String> statuses) {
        Project project = projectService.initializeProjectInfo(projectId);

        List<Task> taskList = taskRepository.findAllFilterByTaskStatus(project, statuses);

        return taskList.stream()
                .map(task -> {
                    User manager = task.getManagerId() != null
                            ? userService.findUserById(task.getManagerId())
                            : null;

                    return TaskResponseConverter.convertToProjectTaskSimpleResponse(task, manager);
                }).collect(Collectors.toList());
    }

    public List<ProjectTaskSimpleResponse> getTasksFilterByManager(Long projectId, List<Long> managers, Boolean notSelected) {
        Project project = projectService.initializeProjectInfo(projectId);

        List<Task> taskList = taskRepository.findAllFilterByManager(project, managers, notSelected);

        return taskList.stream()
                .map(task -> {
                    User manager = task.getManagerId() != null
                            ? userService.findUserById(task.getManagerId())
                            : null;

                    return TaskResponseConverter.convertToProjectTaskSimpleResponse(task, manager);
                }).collect(Collectors.toList());
    }

    public List<ProjectStoryTaskResponse> getTasksGroupByStory(Long projectId) {
        Project project = projectService.initializeProjectInfo(projectId);
        List<Task> taskList = taskRepository.findAllOrderByCreatedAtDesc(project);
        Hibernate.initialize(project.getStories());

        List<ProjectStoryTaskResponse> projectStoryTaskResponseList = new ArrayList<>();
        Map<String, List<ProjectTaskSimpleResponse>> taskMap = new HashMap<>();
        List<ProjectTaskSimpleResponse> emptyStoryTaskList = new ArrayList<>();

        for (Story story : project.getStories()) {
            String title = story.getTitle();
            taskMap.put(title, new ArrayList<>());
            projectStoryTaskResponseList.add(new ProjectStoryTaskResponse(title, taskMap.get(title)));
        }

        for (Task task : taskList) {
            String story = task.getStory() != null
                    ? task.getStory().getTitle()
                    : null;
            User manager = task.getManagerId() != null
                    ? userService.findUserById(task.getManagerId())
                    : null;
            ProjectTaskSimpleResponse projectTaskSimpleResponse = TaskResponseConverter.convertToProjectTaskSimpleResponse(task, manager);

            if (story == null) {
                emptyStoryTaskList.add(projectTaskSimpleResponse);
                continue;
            }

            taskMap.get(story).add(projectTaskSimpleResponse);
        }
        projectStoryTaskResponseList.add(new ProjectStoryTaskResponse(null, emptyStoryTaskList));

        return projectStoryTaskResponseList;
    }

    public List<ProjectTaskSimpleResponse> searchTasksByKeyword(Long projectId, String keyword) {
        Project project = projectService.initializeProjectInfo(projectId);
        List<Task> taskList = taskRepository.findTasksSearchByKeyword(project, keyword);

        return taskList.stream()
                .map(task -> {
                    User manager = task.getManagerId() != null
                            ? userService.findUserById(task.getManagerId())
                            : null;

                    return TaskResponseConverter.convertToProjectTaskSimpleResponse(task, manager);
                }).collect(Collectors.toList());
    }

    public Task findTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);
    }
}
