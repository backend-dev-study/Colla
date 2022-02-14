package kr.kro.colla.comment.domain.repository;

import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.common.fixture.CommentProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.config.JpaAuditingConfig;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void 댓글_등록에_성공한다() {
        // given
        User user = userRepository.save(UserProvider.createUser());
        Comment comment = Comment.builder()
                .user(user)
                .contents("contents")
                .build();

        // when
        Comment result = commentRepository.save(comment);

        // then
        assertThat(result.getContents()).isEqualTo(comment.getContents());
        assertThat(result.getCreateAt()).isNotNull();
        assertThat(result.getCreateAt()).isBefore(now());
        assertThat(result.getUpdatedAt()).isBefore(now());
    }

    @Test
    void 특정_태스크에_속한_댓글들을_모두_조회한다() {
        // given
        User user = userRepository.save(UserProvider.createUser());
        Project project = projectRepository.save(ProjectProvider.createProject(user.getId()));
        Task task = taskRepository.save(TaskProvider.createTaskForRepository(user.getId(), project, null, project.getTaskStatuses().get(0)));
        commentRepository.saveAll(List.of(
                CommentProvider.createComment1(user, task, null),
                CommentProvider.createComment2(user, task, null)
        ));

        // when
        List<Comment> allComments = commentRepository.findAll(task);

        // then
        assertThat(allComments.size()).isEqualTo(2);
        assertThat(allComments).extracting("contents")
                .contains("first comment contents", "second comment contents");
        allComments.forEach(comment -> assertThat(comment.getTask().getId()).isEqualTo(task.getId()));
    }

}
