package kr.kro.colla.task.task.presentation.dto;

import kr.kro.colla.task.task.domain.dto.TaskCountByStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TaskCountResponse {
    private String taskStatusName;
    private Long taskCount;

    public TaskCountResponse(TaskCountByStatus taskCount) {
        this.taskStatusName = taskCount.getTaskStatusName();
        this.taskCount = taskCount.getTaskCount();
    }
}
