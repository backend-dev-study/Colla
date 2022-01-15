package kr.kro.colla.project.project.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.user.user.infrastructure.S3Storage;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final S3Storage s3Storage;
    private final ProjectRepository projectRepository;

    public Project createProject(Long managerId, CreateProjectRequest createProjectRequest) {
        String thumbnailUrl = s3Storage.upload(createProjectRequest.getThumbnail());
        Project project = Project.builder()
                .managerId(managerId)
                .name(createProjectRequest.getName())
                .description(createProjectRequest.getDescription())
                .thumbnail(thumbnailUrl)
                .build();

        return projectRepository.save(project);
    }
}
