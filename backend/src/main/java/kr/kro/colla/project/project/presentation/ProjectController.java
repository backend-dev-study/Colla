package kr.kro.colla.project.project.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.presentation.argument_resolver.Authenticated;
import kr.kro.colla.exception.exception.user.UserNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberDecision;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberRequest;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberResponse;
import kr.kro.colla.project.project.presentation.dto.ProjectResponse;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.notice.domain.NoticeType;
import kr.kro.colla.user.notice.service.NoticeService;
import kr.kro.colla.user.notice.service.dto.CreateNoticeRequest;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.service.UserProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final NoticeService noticeService;
    private final UserProjectService userProjectService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable long projectId) {
        ProjectResponse projectResponse = projectService.getProject(projectId);

        return ResponseEntity.ok(projectResponse);
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity inviteMember(@Authenticated LoginUser loginUser,
                                       @PathVariable long projectId, @Valid @RequestBody ProjectMemberRequest projectMemberRequest){
        Project project = projectService.findProjectById(projectId);
        User manager = userService.findUserById(loginUser.getId());
        // manager 아닐 경우 예외처리

        User user = userService.findByGithubId(projectMemberRequest.getGithubId());
        CreateNoticeRequest createNoticeRequest = CreateNoticeRequest.builder()
                .noticeType(NoticeType.INVITE_USER)
                .receiver(user)
                .build();
        noticeService.createNotice(createNoticeRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{projectId}/members/decision")
    public ResponseEntity DecideInvitation(@Authenticated LoginUser loginUser,
                                           @PathVariable long projectId, @Valid @RequestBody ProjectMemberDecision projectMemberDecision){
        User user = userService.findUserById(loginUser.getId());
        // user 알림 체크 및 읽음 처리
        Project project = projectService.findProjectById(projectId);

        if (projectMemberDecision.isAccept()){
            UserProject userProject = userProjectService.joinProject(user, project);
            return ResponseEntity.ok(new ProjectMemberResponse(userProject));
        }
        return ResponseEntity.noContent().build();

    }
}
