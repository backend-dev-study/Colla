package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.story.domain.Story;
import lombok.Getter;

@Getter
public class ProjectStoryResponse {

    private String title;

    public ProjectStoryResponse(Story story) {
        this.title = story.getTitle();
    }

}
