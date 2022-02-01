package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.task.tag.domain.Tag;
import lombok.Getter;

@Getter
public class CreateTagResponse {

    private String name;

    public CreateTagResponse(Tag tag) {
        this.name = tag.getName();
    }

}
