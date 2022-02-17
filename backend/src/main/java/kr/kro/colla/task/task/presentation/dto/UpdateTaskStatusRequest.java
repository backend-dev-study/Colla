package kr.kro.colla.task.task.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class UpdateTaskStatusRequest {
    @NotNull
    private String statusName;
}
