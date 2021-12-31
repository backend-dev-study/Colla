package kr.kro.colla.user.user.presentation.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
@Builder
public class CreateProjectRequest {
    @NotNull
    private String name;

    private String description;
}
