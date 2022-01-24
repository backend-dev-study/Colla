package kr.kro.colla.project.project.service.dto;

import kr.kro.colla.task.task.domain.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProjectTaskResponse {

    private Long id;

    private String title;

    private String managerName;

    private Integer priority;

    public ProjectTaskResponse(Task task, String managerName) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.managerName = managerName;
        this.priority = task.getPriority();
    }

}