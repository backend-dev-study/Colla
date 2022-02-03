package kr.kro.colla.task.task_tag.service;

import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.tag.service.TagService;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import kr.kro.colla.task.task_tag.domain.repository.TaskTagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskTagServiceTest {

    @Mock
    private TagService tagService;

    @Mock
    private TaskTagRepository taskTagRepository;

    @InjectMocks
    private TaskTagService taskTagService;

    @Test
    void 태스트에_선택한_태그들을_지정한다() {
        // given
        Task task = Task.builder()
                .title("task title")
                .description("task description")
                .taskStatus(new TaskStatus("In Progress"))
                .build();
        String tags = "[\"backend\", \"refactoring\"]";
        List<Tag> tagList = List.of(
                new Tag("backend"),
                new Tag("refactoring")
        );

        given(tagService.findTagsByName(any(List.class)))
                .willReturn(tagList);

        // when
        taskTagService.setTaskTag(task, tags);

        // then
        verify(taskTagRepository, times(2)).save(any(TaskTag.class));
    }

}
