package kr.kro.colla.project.project.repository;

import kr.kro.colla.project.project.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
