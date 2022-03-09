package kr.kro.colla.task.task.service.converter;

import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskResponse;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskSimpleResponse;
import kr.kro.colla.task.task.presentation.dto.RoadmapTaskResponse;
import kr.kro.colla.user.user.domain.User;

import java.util.stream.Collectors;

public class TaskResponseConverter {

    public static ProjectTaskSimpleResponse convertToProjectTaskSimpleResponse(Task task, User manager) {
        return ProjectTaskSimpleResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .managerName(manager != null ? manager.getName() : null)
                .managerAvatar(manager != null ? manager.getAvatar() : null)
                .priority(task.getPriority())
                .status(task.getTaskStatus().getName())
                .tags(task.getTaskTags()
                        .stream()
                        .map(taskTag -> taskTag.getTag().getName())
                        .collect(Collectors.toList()))
                .build();
    }

    public static ProjectTaskResponse convertToProjectTaskResponse(Task task, User user) {
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

    public static RoadmapTaskResponse convertToRoadmapTaskResponse(Task task, User user) {
        return RoadmapTaskResponse.builder()
                .title(task.getTitle())
                .manager(user != null ? user.getName() : null)
                .tags(task.getTaskTags()
                        .stream()
                        .map(taskTag -> taskTag.getTag().getName())
                        .collect(Collectors.toList()))
                .build();
    }

}
