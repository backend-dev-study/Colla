package kr.kro.colla.project.project.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project createProject(Long managerId, CreateProjectRequest createProjectRequest) {
        Project project = Project.builder()
                .managerId(managerId)
                .name(createProjectRequest.getName())
                .description(createProjectRequest.getDescription())
                .build();

        return projectRepository.save(project);
    }
}
