package kr.kro.colla.utils;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ProxyInitializer {
    private final ProjectService projectService;

    @Around(value = "@annotation(proxyInfo)")
    public void initializeProxy(ProceedingJoinPoint pjp, ProxyInitialized proxyInfo) throws Throwable {
        String methodName = pjp.getSignature().getName();
        System.out.println("method name : "+methodName);
        System.out.println("proxy name : " +proxyInfo.proxyTarget());
        pjp.proceed();
    }
}
