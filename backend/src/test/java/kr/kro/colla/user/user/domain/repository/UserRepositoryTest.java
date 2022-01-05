package kr.kro.colla.user.user.domain.repository;

import kr.kro.colla.error.exception.user.UserNotFoundException;
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
    void github아이디로_사용자를_조회할_수_없다() {
        // when, then
        assertThatThrownBy(() -> userRepository.findByGithubId("asdasd")
                .orElseThrow(UserNotFoundException::new))
                .isInstanceOf(UserNotFoundException.class);
    }

}
