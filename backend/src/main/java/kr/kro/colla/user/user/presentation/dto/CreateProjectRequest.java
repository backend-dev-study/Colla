package kr.kro.colla.user.user.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Builder
public class CreateProjectRequest {

    @NotNull
    private String name;

    private String description;

    @Builder
    public CreateProjectRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
