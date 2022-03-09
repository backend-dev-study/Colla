package kr.kro.colla.story.domain.repository;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.StoryProvider;
import kr.kro.colla.exception.exception.story.StoryNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.story.domain.Story;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class StoryRepositoryTest {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void 스토리를_제목으로_조회한다() {
        // given
        String title = "user can login with github";
        Story story = Story.builder()
                .title(title)
                .build();
        storyRepository.save(story);

        // when
        Story result = storyRepository.findByTitle(title)
                .orElseThrow(StoryNotFoundException::new);

        // then
        assertThat(result.getTitle()).isEqualTo(title);
    }

    @Test
    void 프로젝트에_속한_모든_스토리를_조회한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(1L));
        Story story1 = StoryProvider.createStory(project, "first story");
        Story story2 = StoryProvider.createStory(project, "second story");
        storyRepository.saveAll(List.of(story1, story2));

        // when
        List<Story> storyList = storyRepository.findProjectStories(project);

        // then
        assertThat(storyList).hasSize(2);
        assertThat(storyList.get(0).getTitle()).isEqualTo(story1.getTitle());
        assertThat(storyList.get(1).getTitle()).isEqualTo(story2.getTitle());
    }

}
