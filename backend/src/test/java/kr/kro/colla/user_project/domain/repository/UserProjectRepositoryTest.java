package kr.kro.colla.user_project.domain.repository;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import kr.kro.colla.user_project.domain.UserProject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class UserProjectRepositoryTest {
    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    void setUp(){
        User user = User.builder()
                .githubId("binimini")
                .name("subin")
                .avatar("github_avatar").build();
        userRepository.save(user);

        Project project = Project.builder()
                .managerId(123L)
                .name("project_name").build();
        projectRepository.save(project);

        UserProject userProject = UserProject.builder()
                .user(user)
                .project(project)
                .build();
        userProjectRepository.save(userProject);
    }

    @Test
    void 사용자_프로젝트_연결_저장에_성공한다(){
        // given
        UserProject result = userProjectRepository.findAll().get(0);

        // when
        assertThat(result.getId()).isNotNull();
        assertThat(result.getProject().getName()).isEqualTo("project_name");
        assertThat(result.getUser().getGithubId()).isEqualTo("binimini");

    }
}