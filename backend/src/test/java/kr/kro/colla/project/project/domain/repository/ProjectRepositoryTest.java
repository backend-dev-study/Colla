package kr.kro.colla.project.project.domain.repository;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskStatusProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.exception.exception.project.ProjectNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.task_status.domain.TaskStatus;
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

import java.util.List;
import java.util.stream.Collectors;

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

    @Test
    void 프로젝트의_테스크_상태값을_조회한다() {
        // given
        String taskStatusName = "해야할_일_스택은_터지기_직전";
        User user = userRepository.save(UserProvider.createUser());

        Project project = ProjectProvider.createProject(user.getId());
        TaskStatus taskStatus = TaskStatusProvider.createTaskStatus(taskStatusName);
        project.addStatus(taskStatus);
        projectRepository.save(project);

        flushAndClear();

        // when
        Project result = projectRepository.findById(project.getId())
                .orElseThrow(ProjectNotFoundException::new);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTaskStatuses().size()).isEqualTo(4);
        List<String> statusNames = result.getTaskStatuses()
                .stream()
                .map(status->status.getName())
                .collect(Collectors.toList());
        assertThat(statusNames).contains("To Do", "In Progress", "Done", taskStatusName);
    }

    private void flushAndClear() {
        testEntityManager.flush();
        testEntityManager.clear();
    }

}
