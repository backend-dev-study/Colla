package kr.kro.colla.task.task.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
public class ProjectTaskRequest {

    @NotNull
    private Long projectId;

}
