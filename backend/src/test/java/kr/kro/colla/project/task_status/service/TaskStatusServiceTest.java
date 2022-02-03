package kr.kro.colla.project.task_status.service;

import kr.kro.colla.exception.exception.project.task_status.TaskStatusNotFoundException;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.domain.repository.TaskStatusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskStatusServiceTest {

    @Mock
    private TaskStatusRepository taskStatusRepository;

    @InjectMocks
    private TaskStatusService taskStatusService;

    @Test
    void 태스크_상태를_이름으로_조회한다() {
        // given
        String name = "In Progress";
        TaskStatus taskStatus = new TaskStatus(name);

        given(taskStatusRepository.findByName(name))
                .willReturn(Optional.of(taskStatus));

        // when
        TaskStatus result = taskStatusService.findTaskStatusByName(name);

        // then
        assertThat(result.getName()).isEqualTo(name);
        verify(taskStatusRepository, times(1)).findByName(any(String.class));
    }

    @Test
    void 존재하지_않는_이름으로_조회_시_예외가_발생한다() {
        // given
        String name = "invalid name";

        given(taskStatusRepository.findByName(name))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> taskStatusService.findTaskStatusByName(name))
                .isInstanceOf(TaskStatusNotFoundException.class);
    }

}
