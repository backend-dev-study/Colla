package kr.kro.colla.story.presentation;

import kr.kro.colla.story.presentation.dto.ProjectStoryResponse;
import kr.kro.colla.story.presentation.dto.UpdateStoryPeriodRequest;
import kr.kro.colla.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/projects")
@RestController
public class StoryController {

    private final StoryService storyService;

    @GetMapping("/{projectId}/all-stories")
    public ResponseEntity<List<ProjectStoryResponse>> getProjectStories(@PathVariable Long projectId) {
        List<ProjectStoryResponse> storyList = storyService.getProjectStories(projectId);

        return ResponseEntity.ok(storyList);
    }

    @PatchMapping("/stories/{storyId}/period")
    public ResponseEntity<Void> updateStoryPeriod(@PathVariable Long storyId, UpdateStoryPeriodRequest updateStoryPeriodRequest) {
        storyService.updateStoryPeriod(storyId, updateStoryPeriodRequest);

        return ResponseEntity.ok()
                .build();
    }

}
