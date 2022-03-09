package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.story.domain.Story;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProjectStorySimpleResponse {

    private String title;

    public ProjectStorySimpleResponse(Story story) {
        this.title = story.getTitle();
    }

}
