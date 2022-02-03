package kr.kro.colla.task.task.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@AllArgsConstructor
@Getter
public class CreateTaskRequest {

    @NotBlank
    private String title;

    private Long managerId;

    private String description;

    @Min(1) @Max(5)
    private Integer priority;

    @NotBlank
    private String status;

    private String tags;

    @NotNull
    private Long projectId;

    private String story;

    private String preTasks;

}
