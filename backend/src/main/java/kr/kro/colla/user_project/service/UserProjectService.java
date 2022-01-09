package kr.kro.colla.user_project.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.domain.repository.UserProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserProjectService {
    private final UserProjectRepository userProjectRepository;

    public UserProject addUserToProject(User user, Project project){
        UserProject userProject = UserProject.builder()
                .user(user)
                .project(project).build();

        return userProjectRepository.save(userProject);
    }
}
