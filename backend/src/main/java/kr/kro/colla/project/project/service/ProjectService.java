package kr.kro.colla.project.project.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.ProjectRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public Project createProject(Long managerId, String name, String desc) {
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc).build();

        return projectRepository.save(project);
    }
}
