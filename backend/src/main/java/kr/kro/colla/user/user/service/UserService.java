package kr.kro.colla.user.user.service;

import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.exception.exception.user.UserNotFoundException;
import kr.kro.colla.user.notice.domain.Notice;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import kr.kro.colla.user.user.presentation.dto.UserNoticeResponse;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

    public String updateDisplayName(Long id, String displayName) {
        User user = this.userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user.changeDisplayName(displayName);

        return displayName;
    }

    public User createOrUpdateUser(GithubUserProfileResponse userProfile) {
        return this.userRepository.findByGithubId(userProfile.getGithubId())
                .map(user -> user.changeProfile(userProfile))
                .orElseGet(() -> this.userRepository.save(
                                User.builder()
                                        .name(userProfile.getName())
                                        .githubId(userProfile.getGithubId())
                                        .avatar(userProfile.getAvatar())
                                        .build()
                        )
                );
    }

    public List<UserNoticeResponse> getUserNotices(Long id){
        return findUserById(id).getNotices().stream().map(notice-> new UserNoticeResponse(notice)).collect(Collectors.toList());

    }
    public List<UserProjectResponse> getUserProjects(Long id) {
        return findUserById(id).getProjects()
                .stream()
                .map(userProject -> new UserProjectResponse(userProject.getProject()))
                .collect(Collectors.toList());
    }

    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public User findByGithubId(String githubId){
        return userRepository.findByGithubId(githubId)
                .orElseThrow(UserNotFoundException::new);
    }
}
