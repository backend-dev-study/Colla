package kr.kro.colla.task.task.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskCntByStatus {
    private String taskStatusName;
    private Long taskCnt;
}
