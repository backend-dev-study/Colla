package kr.kro.colla.story.domain.repository;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.story.domain.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Long> {

    Optional<Story> findByTitle(String title);

    @Query("select s from Story s where s.project = :project")
    List<Story> findProjectStories(@Param("project")Project project);

}
