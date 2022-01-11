package kr.kro.colla.user_project.domain.repository;

import kr.kro.colla.user_project.domain.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, Long> {
}
