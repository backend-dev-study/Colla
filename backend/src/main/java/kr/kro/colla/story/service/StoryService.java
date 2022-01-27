package kr.kro.colla.story.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.CreateStoryRequest;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.domain.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

}
