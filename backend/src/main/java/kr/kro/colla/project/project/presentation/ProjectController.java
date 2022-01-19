package kr.kro.colla.project.project.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.presentation.argument_resolver.Authenticated;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberRequest;
import kr.kro.colla.project.project.presentation.dto.ProjectMemberResponse;
import kr.kro.colla.project.project.presentation.dto.ProjectResponse;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.service.UserProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;
    private final UserProjectService userProjectService;

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable long projectId) {
        ProjectResponse projectResponse = projectService.getProject(projectId);

        return ResponseEntity.ok(projectResponse);
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity joinMember(@Authenticated LoginUser loginUser,
                                     @PathVariable long projectId, @RequestBody ProjectMemberRequest projectMemberRequest){
        Project project = projectService.findProjectById(projectId);
        User manager = userService.findUserById(loginUser.getId());

        User user = userService.findByGithubId(projectMemberRequest.getGithubId());
        UserProject userProject = userProjectService.joinProject(user, project);
        return ResponseEntity.ok(new ProjectMemberResponse(userProject));
    }
}
