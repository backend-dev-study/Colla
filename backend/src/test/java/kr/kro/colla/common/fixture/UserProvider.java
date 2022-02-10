package kr.kro.colla.common.fixture;

import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserProvider {

    @Autowired
    private UserRepository userRepository;

    public User 가_회원가입을_한다() {
        return userRepository.save(createUser());
    }

    public static User createUser() {
        return User.builder()
                .name("yeongkee")
                .githubId("kykapple")
                .avatar("s3_content")
                .build();
    }

}
