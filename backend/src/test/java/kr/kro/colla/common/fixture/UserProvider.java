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
        User user = User.builder()
                .name("yeongkee")
                .githubId("kykapple")
                .avatar("s3_content")
                .build();

        return userRepository.save(user);
    }

}
