package kr.kro.colla.project.project.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.presentation.argument_resolver.Authenticated;
import kr.kro.colla.project.project.presentation.dto.*;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.service.StoryService;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task.presentation.dto.CreateTaskRequest;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.service.NoticeService;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.service.UserProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final UserService userService;
    private final StoryService storyService;
    private final ProjectService projectService;
    private final UserProjectService userProjectService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable long projectId) {
        ProjectResponse projectResponse = projectService.getProject(projectId);

        return ResponseEntity.ok(projectResponse);
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity inviteUser(@Authenticated LoginUser loginUser,
                                       @PathVariable long projectId, @Valid @RequestBody ProjectMemberRequest projectMemberRequest){
        projectService.inviteUserToProject(projectId, loginUser.getId(), projectMemberRequest.getGithubId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/members/decision")
    public ResponseEntity decideInvitation(@Authenticated LoginUser loginUser,
                                           @PathVariable long projectId, @Valid @RequestBody ProjectMemberDecision projectMemberDecision){
        Optional<ProjectMemberResponse> result = projectService.handleInvitationDecision(projectId, loginUser.getId(), projectMemberDecision);

        return result.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(result.get());

    }

    @PostMapping("/{projectId}/stories")
    public ResponseEntity<ProjectStoryResponse> createStory(@PathVariable Long projectId, @Valid @RequestBody CreateStoryRequest createStoryRequest) {
        Story story = storyService.createStory(projectId, createStoryRequest);

        return ResponseEntity.ok(new ProjectStoryResponse(story));
    }

    @GetMapping("/{projectId}/stories")
    public ResponseEntity<List<ProjectStoryResponse>> getProjectStories(@PathVariable Long projectId) {
        List<ProjectStoryResponse> projectStories = projectService.getProjectStories(projectId);

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

}
