package kr.kro.colla.story.presentation.dto;

import kr.kro.colla.story.domain.Story;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
public class ProjectStoryResponse {

    private Long id;

    private String title;

    private String preStories;

    private LocalDate startAt;

    private LocalDate endAt;

    public ProjectStoryResponse(Story story) {
        this.id = story.getId();
        this.title = story.getTitle();
        this.preStories = story.getPreStories();
        this.startAt = story.getStartAt();
        this.endAt = story.getEndAt();
    }

}
