package kr.kro.colla.user.user.presentation;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import kr.kro.colla.user.user.presentation.dto.CreateProjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final ProjectService projectService;


    @PostMapping("/{userId}/projects")
    public ResponseEntity<CreateProjectResponse> createProject(@PathVariable("userId") Long userId, @RequestBody @Valid CreateProjectRequest createProjectRequest){
        Project project = projectService.createProject(userId, createProjectRequest.getName(), createProjectRequest.getDescription());
        return ResponseEntity.ok(new CreateProjectResponse(project));
    }


}
