package kr.kro.colla.common.fixture;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectProvider {

    @Autowired
    private ProjectRepository projectRepository;

    public Project 를_생성한다(Long managerId) {
        Project project = Project.builder()
                .managerId(managerId)
                .name("project name")
                .description("project description")
                .thumbnail("s3_content")
                .build();

        return projectRepository.save(project);
    }

}
