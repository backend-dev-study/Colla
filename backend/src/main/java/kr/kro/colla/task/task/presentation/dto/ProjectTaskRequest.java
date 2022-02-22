package kr.kro.colla.task.task.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProjectTaskRequest {

    @NotNull
    private Long projectId;

}
