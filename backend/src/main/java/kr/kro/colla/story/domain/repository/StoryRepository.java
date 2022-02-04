package kr.kro.colla.story.domain.repository;

import kr.kro.colla.story.domain.Story;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoryRepository extends JpaRepository<Story, Long> {

    public Optional<Story> findByTitle(String title);

}
