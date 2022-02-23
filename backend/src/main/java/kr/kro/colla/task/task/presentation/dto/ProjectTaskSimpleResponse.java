package kr.kro.colla.task.task.presentation.dto;

import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.user.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTaskSimpleResponse {

    private Long id;

    private String managerName;

    private String managerAvatar;

    private String title;

    private String description;

    private Integer priority;

    private String status;

    private List<String> tags;

    public ProjectTaskSimpleResponse(Task task, User manager) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.priority = task.getPriority();
        this.status = task.getTaskStatus().getName();
        this.tags = task.getTaskTags()
                .stream()
                .map(taskTag -> taskTag.getTag().getName())
                .collect(Collectors.toList());
        if (manager!=null){
            this.managerName = manager.getName();
            this.managerAvatar = manager.getAvatar();
        }
    }
}
