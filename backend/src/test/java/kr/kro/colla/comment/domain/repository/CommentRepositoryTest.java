package kr.kro.colla.comment.domain.repository;

import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.config.JpaAuditingConfig;
import kr.kro.colla.user.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@Import(JpaAuditingConfig.class)
@ActiveProfiles("test")
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void 댓글_등록에_성공한다() {
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
