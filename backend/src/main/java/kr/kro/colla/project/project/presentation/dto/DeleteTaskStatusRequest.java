package kr.kro.colla.project.project.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeleteTaskStatusRequest {
    @NotNull
    private String name;
}
