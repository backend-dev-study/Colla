package kr.kro.colla.project.project.domain.repository;

import kr.kro.colla.exception.exception.project.ProjectNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import kr.kro.colla.user_project.domain.UserProject;
import kr.kro.colla.user_project.domain.repository.UserProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Long managerId = 1L;
    private String name = "생성된 프로젝트", desc = "설명";

    @Test
    void 프로젝트_생성을_성공한다() {
        // given
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .build();

        // when
        Project result = projectRepository.save(project);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(name);
        assertThat(result.getTaskStatuses().size()).isEqualTo(3);

    }

    @Test
    void 프로젝트_생성을_실패한다() {
        // given
        Project project = Project.builder()
                .build();

        // when
        assertThatThrownBy(()-> projectRepository.save(project))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void 프로젝트_멤버를_조회한다() {
        // given
        User user = User.builder()
                .name("yeongkee")
                .githubId("kykapple")
                .avatar("github_content")
                .build();
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .build();
        userRepository.save(user);
        projectRepository.save(project);

        UserProject userProject = UserProject.builder()
                .project(project)
                .user(user)
                .build();
        userProjectRepository.save(userProject);

        flushAndClear();

        // when
        Project result = projectRepository.findById(project.getId())
                .orElseThrow(ProjectNotFoundException::new);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getMembers().size()).isEqualTo(1);

    }

    private void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

}
