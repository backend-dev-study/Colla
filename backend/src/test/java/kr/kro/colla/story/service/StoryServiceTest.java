package kr.kro.colla.story.service;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.StoryProvider;
import kr.kro.colla.exception.exception.story.StoryNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.CreateStoryRequest;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.domain.repository.StoryRepository;
import kr.kro.colla.story.presentation.dto.ProjectStoryResponse;
import kr.kro.colla.story.presentation.dto.UpdateStoryPeriodRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
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

    @Test
    void 스토리를_제목으로_조회한다() {
        // given
        String title = "user can login with github";
        Story story = Story.builder()
                .title(title)
                .build();

        given(storyRepository.findByTitle(title))
                .willReturn(Optional.of(story));

        // when
        Story result = storyService.findStoryByTitle(title);

        // then
        assertThat(result.getTitle()).isEqualTo(title);
        verify(storyRepository, times(1)).findByTitle(any(String.class));
    }

    @Test
    void 존재하지_않는_스토리_제목으로_조회_시_예외가_발생한다() {
        // given
        String title = "invalid title";

        given(storyRepository.findByTitle(title))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> storyService.findStoryByTitle(title))
                .isInstanceOf(StoryNotFoundException.class);
    }

    @Test
    void 프로젝트에_속한_스토리를_모두_조회한다() {
        // given
        Long projectId = 5L;
        Project project = ProjectProvider.createProject(1L);
        List<Story> storyList = List.of(
                StoryProvider.createStory(project, "first story"),
                StoryProvider.createStory(project, "second story")
        );

        given(projectService.findProjectById(eq(projectId)))
                .willReturn(project);
        given(storyRepository.findProjectStories(any(Project.class)))
                .willReturn(storyList);

        // when
        List<ProjectStoryResponse> result = storyService.getProjectStories(projectId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo(storyList.get(0).getTitle());
        assertThat(result.get(1).getTitle()).isEqualTo(storyList.get(1).getTitle());
        verify(projectService, times(1)).findProjectById(anyLong());
        verify(storyRepository, times(1)).findProjectStories(any(Project.class));
    }

    @Test
    void 스토리의_진행_기간을_설정한다() {
        // given
        Long storyId = 1L;
        Project project = ProjectProvider.createProject(5L);
        Story story = StoryProvider.createStory(project, "story title");
        UpdateStoryPeriodRequest updateStoryPeriodRequest = new UpdateStoryPeriodRequest(LocalDate.of(2022, 3, 9), LocalDate.of(2022, 3, 12));

        given(storyRepository.findById(eq(storyId)))
                .willReturn(Optional.of(story));

        // when
        storyService.updateStoryPeriod(storyId, updateStoryPeriodRequest);

        // then
        assertThat(story.getStartAt()).isEqualTo(updateStoryPeriodRequest.getStartAt());
        assertThat(story.getEndAt()).isEqualTo(updateStoryPeriodRequest.getEndAt());
        verify(storyRepository, times(1)).findById(anyLong());
    }

}
