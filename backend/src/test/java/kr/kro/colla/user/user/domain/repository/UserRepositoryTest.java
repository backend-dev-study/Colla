package kr.kro.colla.user.user.domain.repository;

import kr.kro.colla.exception.exception.user.UserNotFoundException;
import kr.kro.colla.user.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("kyk")
                .githubId("kykapple")
                .avatar("avatar.githubcontent")
                .build();

        userRepository.save(user);
    }

    @Test
    void github아이디로_사용자를_조회한다() {
        // when
        User user = userRepository.findByGithubId("kykapple")
                .orElseThrow(UserNotFoundException::new);

        // then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getName()).isEqualTo(user.getName());
        assertThat(user.getGithubId()).isEqualTo(user.getGithubId());
        assertThat(user.getAvatar()).isEqualTo(user.getAvatar());
    }

    @Test
    void 존재하지_않는_github아이디로_사용자를_조회할_경우_예외를_반환한다() {
        // when, then
        assertThatThrownBy(
                () -> userRepository.findByGithubId("asdasd")
                        .orElseThrow(UserNotFoundException::new)
        ).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 사용자의_닉네임을_변경할_수_있다() {
        // when
        String newDisplayName = "new-name";
        User findUser = userRepository.findById(user.getId())
                .orElseThrow(UserNotFoundException::new);
        findUser.changeDisplayName(newDisplayName);

        User result = userRepository.findById(user.getId())
                .orElseThrow(UserNotFoundException::new);

        // then
        assertThat(result.getName()).isEqualTo(newDisplayName);
        assertThat(result.getGithubId()).isEqualTo(user.getGithubId());
        assertThat(result.getAvatar()).isEqualTo(user.getAvatar());
    }

    @Test
    void 잘못된_id로_조회할_경우_예외가_발생한다() {
        // given
        Long inValidId = 3L;

        // when, then
        assertThatThrownBy(
                () -> userRepository.findById(inValidId)
                        .orElseThrow(UserNotFoundException::new)
        ).isInstanceOf(UserNotFoundException.class);
    }

}
