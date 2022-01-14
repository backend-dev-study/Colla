package kr.kro.colla.user.user.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.presentation.argument_resolver.Authenticated;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.*;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user.user.service.dto.UserProjectResponseDto;
import kr.kro.colla.user_project.service.UserProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProjectService projectService;
    private final UserProjectService userProjectService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(@Authenticated LoginUser loginUser) {
        User user = this.userService.findUserById(loginUser.getId());

        return ResponseEntity.ok(new UserProfileResponse(user));
    }

    @PatchMapping("/name")
    public ResponseEntity<UpdateUserNameResponse> updateDisplayName(
            @Authenticated LoginUser loginUser,
            @RequestBody UpdateUserNameRequest updateUserNameRequest
    ) {
        String updatedName = this.userService.updateDisplayName(loginUser.getId(), updateUserNameRequest.getName());

        return ResponseEntity.ok(new UpdateUserNameResponse(updatedName));
    }

    @PostMapping("/projects")
    public ResponseEntity<CreateProjectResponse> createProject(
            @Authenticated LoginUser loginUser,
            @Valid @RequestBody CreateProjectRequest createProjectRequest
    ) {
        User user = userService.findUserById(loginUser.getId());
        Project project = projectService.createProject(loginUser.getId(), createProjectRequest);
        userProjectService.joinProject(user, project);

        return ResponseEntity.ok(new CreateProjectResponse(project));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<UserProjectResponse>> getUserProject(@Authenticated LoginUser loginUser) {
        List<UserProjectResponseDto> userProjectResponseDtoList = userService.getUserProject(loginUser.getId());

        return ResponseEntity.ok(UserProjectResponse.of(userProjectResponseDtoList));
    }

}
