package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.project.task_status.domain.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProjectTaskStatusResponse {
    private Long id;

    private String name;

    public ProjectTaskStatusResponse(TaskStatus taskStatus) {
        this.id = taskStatus.getId();
        this.name = taskStatus.getName();
    }
}
