package kr.kro.colla.user.user.service.dto;

import kr.kro.colla.project.project.domain.Project;
import lombok.Getter;

@Getter
public class UserProjectResponseDto {

    private Long id;

    private Long managerId;

    private String name;

    private String description;

    public String thumbnail;

    public UserProjectResponseDto(Project project) {
        this.id = project.getId();
        this.managerId = project.getManagerId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.thumbnail = project.getThumbnail();
    }

}
