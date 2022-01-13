package kr.kro.colla.user.user.service;

import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.exception.exception.user.UserNotFoundException;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;

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

    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    public String updateDisplayName(Long id, String displayName) {
        User user = this.userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        user.changeDisplayName(displayName);

        return displayName;
    }

}
