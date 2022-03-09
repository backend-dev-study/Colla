package kr.kro.colla.story.service;

import kr.kro.colla.exception.exception.story.StoryNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.CreateStoryRequest;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.domain.repository.StoryRepository;
import kr.kro.colla.story.presentation.dto.ProjectStoryResponse;
import kr.kro.colla.story.presentation.dto.UpdateStoryPeriodRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class StoryService {

    private final ProjectService projectService;
    private final StoryRepository storyRepository;

    public Story createStory(Long projectId, CreateStoryRequest createStoryRequest) {
        Project project = projectService.findProjectById(projectId);
        Story story = Story.builder()
                .title(createStoryRequest.getTitle())
                .preStories("[]")
                .project(project)
                .build();

        return storyRepository.save(story);
    }

    public void updateStoryPeriod(Long storyId, UpdateStoryPeriodRequest updateStoryPeriodRequest) {
        Story story = findStoryById(storyId);
        story.updatePeriod(updateStoryPeriodRequest);
    }

    public List<ProjectStoryResponse> getProjectStories(Long projectId) {
        Project project = projectService.findProjectById(projectId);
        List<Story> storyList = storyRepository.findProjectStories(project);

        return storyList.stream()
                .map(ProjectStoryResponse::new)
                .collect(Collectors.toList());
    }

    public Story findStoryById(Long id) {
        return storyRepository.findById(id)
                .orElseThrow(StoryNotFoundException::new);
    }

    public Story findStoryByTitle(String title) {
        return storyRepository.findByTitle(title)
                .orElseThrow(StoryNotFoundException::new);
    }

}
