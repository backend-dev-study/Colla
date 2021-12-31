package kr.kro.colla.user.user.presentation.dto;

import kr.kro.colla.project.project.domain.Project;
import lombok.Getter;

@Getter
public class CreateProjectResponse {
    private Long id;

    private Long managerId;

    private String name;

    private String description;

    public CreateProjectResponse(Project project){
        this.id = project.getId();
        this.managerId = project.getManagerId();
        this.name = project.getName();
        this.description = project.getDescription();
    }
}
