package kr.kro.colla.user.user.presentation.dto;

import kr.kro.colla.project.project.domain.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserProjectResponse {

    private Long id;

    private Long managerId;

    private String name;

    private String description;

    public String thumbnail;

    public UserProjectResponse(Project project) {
        this.id = project.getId();
        this.managerId = project.getManagerId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.thumbnail = project.getThumbnail();
    }

}
