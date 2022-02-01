package kr.kro.colla.task.tag.service;

import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.tag.domain.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagService tagService;

    @Test
    void 태그_등록에_성공한다() {
        // given
        String tagName = "backend";
        Tag tag = new Tag(tagName);

        given(tagRepository.findByName(tagName))
                .willReturn(Optional.empty());
        given(tagRepository.save(any(Tag.class)))
                .willReturn(tag);

        // when
        Tag result = tagService.createTagIfNotExist(tagName);

        // then
        verify(tagRepository, times(1)).findByName(any(String.class));
        verify(tagRepository, times(1)).save(any(Tag.class));
        assertThat(result.getName()).isEqualTo(tag.getName());
    }

    @Test
    void 동일한_태그가_존재할_시_등록되지_않는다() {
        // given
        String tagName = "backend";
        Tag tag = new Tag(tagName);

        given(tagRepository.findByName(tagName))
                .willReturn(Optional.of(tag));

        // when
        Tag result = tagService.createTagIfNotExist(tagName);

        // then
        verify(tagRepository, times(1)).findByName(any(String.class));
        verify(tagRepository, never()).save(any(Tag.class));
        assertThat(result.getName()).isEqualTo(tag.getName());
    }

}
