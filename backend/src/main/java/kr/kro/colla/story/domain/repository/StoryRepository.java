package kr.kro.colla.story.domain.repository;

import kr.kro.colla.story.domain.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long> {
}
