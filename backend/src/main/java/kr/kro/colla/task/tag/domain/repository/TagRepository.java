package kr.kro.colla.task.tag.domain.repository;

import kr.kro.colla.task.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    public Optional<Tag> findByName(String name);

}
