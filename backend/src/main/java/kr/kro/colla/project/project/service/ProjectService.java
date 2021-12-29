package kr.kro.colla.project.project.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.repository.ProjectRepository;
import kr.kro.colla.project.project.service.dto.CreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project createProject(Long managerId, CreateRequest createRequest) {
        Project project = Project.builder()
                .managerId(managerId)
                .name(createRequest.getName())
                .description(createRequest.getDescription()).build();

        return projectRepository.save(project);
    }
}
