package kr.kro.colla.user.user.presentation.dto;

import lombok.Builder;
import lombok.Getter;
import javax.validation.constraints.NotEmpty;

@Getter
@Builder
public class CreateProjectRequest {
    @NotEmpty
    private String name;

    private String description;

    @Builder
    public CreateProjectRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
