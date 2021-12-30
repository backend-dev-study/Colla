package kr.kro.colla.user.user.controller;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.user.controller.dto.CreateProjectRequest;
import kr.kro.colla.user.user.controller.dto.CreateProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final ProjectService projectService;


    @PostMapping("/users/{userId}/projects")
    public ResponseEntity<CreateProjectResponse> createProject(@PathVariable(required = true) Long userId, @RequestBody @Valid CreateProjectRequest createProjectRequest){
        System.out.println(userId+" "+createProjectRequest.getName()+" "+createProjectRequest.getDescription());
        Project project = projectService.createProject(userId, createProjectRequest.getName(), createProjectRequest.getDescription());
        return ResponseEntity.ok(new CreateProjectResponse(project));
    }


}
