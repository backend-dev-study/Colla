package kr.kro.colla.project.project.service;

import kr.kro.colla.exception.exception.project.ProjectNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.profile.ProjectProfileStorage;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.project.presentation.dto.ProjectResponse;
import kr.kro.colla.project.project.service.dto.ProjectTaskResponse;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
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

    public ProjectResponse getProject(Long projectId) {
        Project project = findProjectById(projectId);

        return ProjectResponse.builder()
                .id(project.getId())
                .managerId(project.getManagerId())
                .name(project.getName())
                .description(project.getDescription())
                .thumbnail(project.getThumbnail())
                .members(project.getMembers()
                        .stream()
                        .map(userProject -> new UserProfileResponse(userProject.getUser()))
                        .collect(Collectors.toList()))
                .tasks(project.getTasks()
                        .stream()
                        .map(task -> new ProjectTaskResponse(task))
                        .collect(Collectors.toList()))
                .build();
    }

    public Project findProjectById(Long projectId){
        return projectRepository.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
    }

}
