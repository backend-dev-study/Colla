package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.story.domain.Story;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ProjectStoryResponse {

    private String title;

    public ProjectStoryResponse(Story story) {
        this.title = story.getTitle();
    }

}
