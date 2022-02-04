package kr.kro.colla.user.user.service;

import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.exception.exception.user.UserNotFoundException;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import kr.kro.colla.user_project.service.UserProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private ProjectService projectService;

    @Mock
    private UserProjectService userProjectService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void 첫_로그인_시_회원가입을_진행한다() {
        // given
        GithubUserProfileResponse githubUserProfileResponse = new GithubUserProfileResponse("user", "kykapple", "avatar");
        User user = User.builder()
                .name("user")
                .githubId("githubId")
                .avatar("avatar")
                .build();

        given(userRepository.findByGithubId(githubUserProfileResponse.getGithubId()))
                .willReturn(Optional.empty());
        given(userRepository.save(any(User.class)))
                .willReturn(user);

        // when
        User result = userService.createOrUpdateUser(githubUserProfileResponse);

        // then
        verify(userRepository, times(1)).findByGithubId(any(String.class));
        verify(userRepository, times(1)).save(any(User.class));
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getGithubId()).isEqualTo(user.getGithubId());
        assertThat(result.getAvatar()).isEqualTo(user.getAvatar());
    }

    @Test
    void 기존_사용자일_경우_회원가입을_진행하지_않는다() {
        // given
        GithubUserProfileResponse githubUserProfileResponse = new GithubUserProfileResponse("user", "kykapple", "avatar");
        User user = User.builder()
                .name("user")
                .githubId("githubId")
                .avatar("avatar")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);

        given(userRepository.findByGithubId(githubUserProfileResponse.getGithubId()))
                .willReturn(Optional.of(user));

        // when
        User result = userService.createOrUpdateUser(githubUserProfileResponse);

        // then
        verify(userRepository, times(1)).findByGithubId(any(String.class));
        verify(userRepository, times(0)).save(any(User.class));
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getGithubId()).isEqualTo(user.getGithubId());
        assertThat(result.getAvatar()).isEqualTo(user.getAvatar());
    }

    @Test
    void 사용자의_닉네임을_변경한다() {
        // given
        Long id = 1L;
        String newDisplayName = "new-name";
        User user = User.builder()
                .name("name")
                .githubId("kykapple")
                .avatar("avatar")
                .build();

        given(userRepository.findById(id))
                .willReturn(Optional.of(user));

        // when
        String updatedName = userService.updateDisplayName(id, newDisplayName);

        // then
        assertThat(updatedName).isEqualTo(newDisplayName);
        verify(userRepository, times(1)).findById(eq(id));
    }

    @Test
    void 올바르지_않은_사용자일_경우_예외가_발생한다() {
        // given
        Long inValidId = 3L;
        String newDisplayName = "new-name";

        given(userRepository.findById(inValidId))
                .willThrow(UserNotFoundException.class);

        // when, then
        assertThatThrownBy(() -> userService.updateDisplayName(inValidId, newDisplayName))
                .isInstanceOf(UserNotFoundException.class);
    }

}
