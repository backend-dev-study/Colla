package kr.kro.colla.task.task_status_log.presentation.dto;

import kr.kro.colla.task.task_status_log.domain.TaskStatusLog;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class TaskStatusLogResponse {

    private Integer count;

    private LocalDate createdAt;

    public TaskStatusLogResponse(TaskStatusLog taskStatusLog) {
        this.count = taskStatusLog.getCount();
        this.createdAt = taskStatusLog.getCreatedAt();
    }

}
