package kr.kro.colla.user.user.domain.repository;

import kr.kro.colla.user.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByGithubId(String githubId);
}
