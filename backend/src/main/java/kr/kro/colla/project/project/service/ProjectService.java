package kr.kro.colla.project.project.service;

import kr.kro.colla.exception.exception.project.ProjectNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.profile.ProjectProfileStorage;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectProfileStorage projectProfileStorage;

    public Project createProject(Long managerId, CreateProjectRequest createProjectRequest) {
        String thumbnailUrl = projectProfileStorage.upload(createProjectRequest.getThumbnail());
        Project project = Project.builder()
                .managerId(managerId)
                .name(createProjectRequest.getName())
                .description(createProjectRequest.getDescription())
                .thumbnail(thumbnailUrl)
                .build();

        return projectRepository.save(project);
    }

    public Project findProjectById(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }
}
