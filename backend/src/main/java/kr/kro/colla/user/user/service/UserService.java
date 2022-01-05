package kr.kro.colla.user.user.service;

import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User createUserIfNotExist(GithubUserProfileResponse userProfile) {
        return this.userRepository.findByGithubId(userProfile.getGithubId())
                .orElseGet(() -> this.userRepository.save(
                                User.builder()
                                        .name(userProfile.getName())
                                        .githubId(userProfile.getGithubId())
                                        .avatar(userProfile.getAvatar())
                                        .build()
                        )
                );
    }
}
