package kr.kro.colla.project.project.presentation;

import kr.kro.colla.project.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProjectController {
    private final ProjectService projectService;
}
