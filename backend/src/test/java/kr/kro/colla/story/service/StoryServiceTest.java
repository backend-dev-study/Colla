package kr.kro.colla.story.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.CreateStoryRequest;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.domain.repository.StoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoryServiceTest {

    @Mock
    private StoryRepository storyRepository;

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private StoryService storyService;

    @Test
    void 스토리를_생성한다() {
        // given
        Long projectId = 1L;
        String title = "story title";
        CreateStoryRequest createStoryRequest = new CreateStoryRequest(title);
        Project project = Project.builder()
                .name("project name")
                .description("project description")
                .build();
        ReflectionTestUtils.setField(project, "id", 1L);
        Story story = Story.builder()
                .title(createStoryRequest.getTitle())
                .preStories("[]")
                .project(project)
                .build();

        given(projectService.findProjectById(projectId))
                .willReturn(project);
        given(storyRepository.save(any(Story.class)))
                .willReturn(story);

        // when
        Story result = storyService.createStory(projectId, createStoryRequest);

        // then
        verify(storyRepository, times(1)).save(any(Story.class));
        assertThat(result.getTitle()).isEqualTo(story.getTitle());
        assertThat(result.getPreStories()).isEqualTo("[]");
        assertThat(result.getProject().getId()).isEqualTo(1L);
    }

}
