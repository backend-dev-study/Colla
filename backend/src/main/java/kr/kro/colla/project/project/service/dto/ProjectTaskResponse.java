package kr.kro.colla.project.project.service.dto;

import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.user.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProjectTaskResponse {

    private Long id;

    private String title;

    private String managerName;

    private String avatar;

    private Integer priority;

    public ProjectTaskResponse(Task task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.priority = task.getPriority();
    }

    public ProjectTaskResponse(Task task, User manager) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.managerName = manager.getName();
        this.avatar = manager.getAvatar();
        this.priority = task.getPriority();
    }

}
