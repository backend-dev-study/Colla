package kr.kro.colla.utils;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ProxyInitializer {
    private final ProjectService projectService;

    public Project initializeProjectMemberProxy(Long projectId) {
        Project project = projectService.findProjectById(projectId);
        Hibernate.initialize(project.getMembers());

        return project;
    }

    public Project initializeProjectMemberAndStatusProxy(Long projectId) {
        Project project = initializeProjectMemberProxy(projectId);
        Hibernate.initialize(project.getTaskStatuses());

        return project;
    }
}
