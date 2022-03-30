package kr.kro.colla.task.task.domain.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class TaskCountByStatus {

    @NotNull
    private String taskStatusName;

    @NotNull
    private Long taskCount;

    private String manager;

    public TaskCountByStatus(String taskStatusName, Long taskCount) {
        this.taskStatusName = taskStatusName;
        this.taskCount = taskCount;
    }
}
