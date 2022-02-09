package kr.kro.colla.common.fixture;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.domain.repository.StoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoryProvider {

    @Autowired
    private StoryRepository storyRepository;

    public Story 를_생성한다(Project project) {
        Story story = Story.builder()
                .title("story title")
                .preStories("[]")
                .project(project)
                .build();

        return storyRepository.save(story);
    }

}
