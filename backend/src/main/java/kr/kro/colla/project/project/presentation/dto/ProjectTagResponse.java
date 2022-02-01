package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.task.tag.domain.Tag;
import lombok.Getter;

@Getter
public class ProjectTagResponse {

    private String name;

    public ProjectTagResponse(Tag tag) {
        this.name = tag.getName();
    }

}
