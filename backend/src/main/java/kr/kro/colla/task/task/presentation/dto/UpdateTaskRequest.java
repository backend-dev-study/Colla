package kr.kro.colla.task.task.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@Getter
public class UpdateTaskRequest {

    @NotBlank
    private String title;

    private String managerId;

    private String description;

    @Min(1) @Max(5)
    private Integer priority;

    private String tags;

    @NotNull
    private Long projectId;

    private String story;

    private String preTasks;

}
