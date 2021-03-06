package kr.kro.colla.utils;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ProxyInitializer {
    private final ProjectService projectService;

    @Before("@annotation(proxyInitialized) && args(id,..)")
    public void initializeProxy(ProxyInitialized proxyInitialized, Long id) throws Throwable {
        String target = proxyInitialized.target();

        if (target.contains("Project")) {
            initializeProjectProxy(id, target);
        }
    }

    public void initializeProjectProxy(Long id, String type) {
        Project project = projectService.findProjectById(id);

        switch (type) {
            case "ProjectInfo":
                Hibernate.initialize(project.getStories());
            case "ProjectMemberAndTaskStatus":
                Hibernate.initialize(project.getTaskStatuses());
            case "ProjectMember":
                Hibernate.initialize(project.getMembers());
            default:
                break;
        }
    }
}
