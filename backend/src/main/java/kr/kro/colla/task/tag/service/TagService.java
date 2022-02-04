package kr.kro.colla.task.tag.service;

import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.tag.domain.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class TagService {

    private final TagRepository tagRepository;

    public Tag createTagIfNotExist(String tagName) {
        Tag tag = new Tag(tagName);

        return tagRepository.findByName(tag.getName())
                .orElseGet(() -> tagRepository.save(tag));
    }

    public List<Tag> findTagsByName(List<String> tagNames) {
        return tagRepository.findByNameIn(tagNames);
    }

}
