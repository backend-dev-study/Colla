package kr.kro.colla.project.project.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.presentation.argument_resolver.Authenticated;
import kr.kro.colla.project.project.presentation.dto.*;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.service.StoryService;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final StoryService storyService;
    private final ProjectService projectService;
    private final TaskService taskService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable Long projectId) {
        ProjectResponse projectResponse = projectService.getProject(projectId);

        return ResponseEntity.ok(projectResponse);
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity inviteUser(@Authenticated LoginUser loginUser,
                                       @PathVariable Long projectId, @Valid @RequestBody ProjectMemberRequest projectMemberRequest){
        projectService.inviteUserToProject(projectId, loginUser.getId(), projectMemberRequest.getGithubId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/members/decision")
    public ResponseEntity decideInvitation(@Authenticated LoginUser loginUser,
                                           @PathVariable Long projectId, @Valid @RequestBody ProjectMemberDecision projectMemberDecision){
        ProjectMemberResponse projectMemberResponse = projectService.decideInvitation(projectId, loginUser.getId(), projectMemberDecision);

        return ResponseEntity.ok(projectMemberResponse);

    }

    @PostMapping("/{projectId}/stories")
    public ResponseEntity<ProjectStorySimpleResponse> createStory(@PathVariable Long projectId, @Valid @RequestBody CreateStoryRequest createStoryRequest) {
        Story story = storyService.createStory(projectId, createStoryRequest);

        return ResponseEntity.ok(new ProjectStorySimpleResponse(story));
    }

    @GetMapping("/{projectId}/stories")
    public ResponseEntity<List<ProjectStorySimpleResponse>> getProjectStories(@PathVariable Long projectId) {
        List<ProjectStorySimpleResponse> projectStories = projectService.getProjectStories(projectId);

        return ResponseEntity.ok(projectStories);
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<List<ProjectMemberResponse>> getProjectMembers(@PathVariable Long projectId) {
        List<ProjectMemberResponse> projectMembers = projectService.getProjectMembers(projectId);

        return ResponseEntity.ok(projectMembers);
    }

    @PostMapping("/{projectId}/tags")
    public ResponseEntity<ProjectTagResponse> createTag(@PathVariable Long projectId, @Valid @RequestBody CreateTagRequest createTagRequest) {
        Tag tag = projectService.createTag(projectId, createTagRequest);

        return ResponseEntity.ok(new ProjectTagResponse(tag));
    }

    @GetMapping("/{projectId}/tags")
    public ResponseEntity<List<ProjectTagResponse>> getProjectTags(@PathVariable Long projectId) {
        List<ProjectTagResponse> projectTags = projectService.getProjectTags(projectId);

        return ResponseEntity.ok(projectTags);
    }

    @PostMapping("/{projectId}/statuses")
    public ResponseEntity<ProjectTaskStatusResponse> createTaskStatus(@PathVariable Long projectId, @Valid @RequestBody CreateTaskStatusRequest createRequest) {
        TaskStatus taskStatus = projectService.createTaskStatus(projectId, createRequest.getName());

        return ResponseEntity.ok(new ProjectTaskStatusResponse(taskStatus));
    }

    @DeleteMapping("/{projectId}/statuses")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long projectId, @Valid @RequestBody DeleteTaskStatusRequest deleteRequest) {
        taskService.deleteTaskStatus(projectId, deleteRequest.getFrom(), deleteRequest.getTo());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/statuses")
    public ResponseEntity<List<ProjectTaskStatusResponse>> getTaskStatuses(@PathVariable Long projectId) {
        List<ProjectTaskStatusResponse> projectTaskStatuses = projectService.getTaskStatuses(projectId);

        return ResponseEntity.ok(projectTaskStatuses);
    }
}
