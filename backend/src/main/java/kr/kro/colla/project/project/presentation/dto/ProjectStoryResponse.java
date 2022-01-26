package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.story.domain.Story;
import lombok.Getter;

@Getter
public class ProjectStoryResponse {

    private Long id;

    private String title;

    public ProjectStoryResponse(Story story) {
        this.id = story.getId();
        this.title = story.getTitle();
    }

}
