package kr.kro.colla.common.fixture;

import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProvider {

    @Autowired
    private UserRepository userRepository;

    public User 가_로그인을_한다1() {
        return userRepository.save(createUser());
    }

    public User 가_로그인을_한다2() {
        return userRepository.save(createUser2());
    }

    public static User createUser() {
        return User.builder()
                .name("yeongkee")
                .githubId("kykapple")
                .avatar("s3_content")
                .build();
    }

    public static User createUser2() {
        return User.builder()
                .name("subin")
                .githubId("binimini")
                .avatar("s3_content")
                .build();
    }

}
