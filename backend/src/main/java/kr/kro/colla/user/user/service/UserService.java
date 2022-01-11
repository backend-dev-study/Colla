package kr.kro.colla.user.user.service;

import kr.kro.colla.auth.infrastructure.dto.GithubUserProfileResponse;
import kr.kro.colla.exception.exception.user.UserNotFoundException;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public User findUserById(Long id){
        Optional<User> isUser = userRepository.findById(id);
        if (isUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        return isUser.get();
    }
}
