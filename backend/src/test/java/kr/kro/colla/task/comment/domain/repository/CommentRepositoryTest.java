package kr.kro.colla.task.comment.domain.repository;

import kr.kro.colla.task.comment.domain.Comment;
import kr.kro.colla.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void 댓글_등록_성공() {
        // given
        User user = User.builder()
                .githubId("id")
                .name("name")
                .avatar("avatar")
                .build();
        Comment comment = Comment.builder()
                .user(user)
                .contents("contents")
                .build();

        // when
        Comment result = commentRepository.save(comment);

        // then
        assertThat(result.getCreateAt()).isNotNull();
        assertThat(result.getCreateAt()).isBefore(now());
        assertThat(result.getUpdatedAt()).isBefore(now());
    }

}