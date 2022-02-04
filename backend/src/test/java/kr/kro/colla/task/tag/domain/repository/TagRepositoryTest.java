package kr.kro.colla.task.tag.domain.repository;

import kr.kro.colla.task.tag.domain.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @Test
    void 태그_등록에_성공한다() {
        // given
        Tag tag = new Tag("backend");

        // when
        Tag result = tagRepository.save(tag);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo(tag.getName());
    }

    @Test
    void 태그_목록에_포함된_태그들을_조회한다() {
        // given
        List<String> tagNames = List.of("backend", "frontend");
        tagRepository.saveAll(List.of(
                new Tag("backend"),
                new Tag("frontend"),
                new Tag("refactoring")
        ));

        // when
        List<Tag> tags = tagRepository.findByNameIn(tagNames);

        // then
        assertThat(tags.size()).isEqualTo(2);
        assertThat(tags).extracting("name")
                .contains("backend", "frontend");
    }

}
