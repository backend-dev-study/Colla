package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.story.domain.Story;
import lombok.Getter;

@Getter
public class CreateStoryResponse {

    private String title;

    public CreateStoryResponse(Story story) {
        this.title = story.getTitle();
    }

}
