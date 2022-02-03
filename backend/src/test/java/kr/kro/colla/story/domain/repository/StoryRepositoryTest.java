package kr.kro.colla.story.domain.repository;

import kr.kro.colla.exception.exception.story.StoryNotFoundException;
import kr.kro.colla.story.domain.Story;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class StoryRepositoryTest {

    @Autowired
    private StoryRepository storyRepository;

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

}
