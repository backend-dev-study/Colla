package kr.kro.colla.task.task.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public class CreateTaskRequest {

    private String title;

    private Long managerId;

    private String description;

    private String priority;

    private String status;

    private String tags;

    private Long projectId;

    private String story;

    private String preTasks;

}
