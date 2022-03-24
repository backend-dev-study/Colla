package kr.kro.colla.task.task.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class TaskCntByStatus {

    @NotNull
    private String taskStatusName;

    @NotNull
    private Long taskCnt;

    private String manager;

    public TaskCntByStatus(String taskStatusName, Long taskCnt) {
        this.taskStatusName = taskStatusName;
        this.taskCnt = taskCnt;
    }
}
