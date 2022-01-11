package kr.kro.colla.user.user.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.presentation.argument_resolver.Authenticated;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import kr.kro.colla.user.user.presentation.dto.CreateProjectResponse;
import kr.kro.colla.user.user.presentation.dto.UpdateUserNameRequest;
import kr.kro.colla.user.user.presentation.dto.UpdateUserNameResponse;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.service.UserProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;
    private final UserProjectService userProjectService;

    @PostMapping("/{userId}/projects")
    public ResponseEntity<CreateProjectResponse> createProject(
            @PathVariable("userId") Long userId,
            @Valid @RequestBody CreateProjectRequest createProjectRequest
    ) {
        User user = userService.findUserById(userId);
        Project project = projectService.createProject(userId, createProjectRequest);
        userProjectService.addUserToProject(user, project);

        return ResponseEntity.ok(new CreateProjectResponse(project));
    }

    @PatchMapping("/name")
    public ResponseEntity<UpdateUserNameResponse> updateDisplayName(
            @Authenticated LoginUser loginUser,
            @RequestBody UpdateUserNameRequest updateUserNameRequest
    ) {
        String updatedName = this.userService.updateDisplayName(loginUser.getId(), updateUserNameRequest.getName());
        return ResponseEntity.ok(new UpdateUserNameResponse(updatedName));
    }

}
