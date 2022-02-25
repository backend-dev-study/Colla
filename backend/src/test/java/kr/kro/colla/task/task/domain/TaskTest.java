package kr.kro.colla.task.task.domain;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.task.presentation.dto.UpdateTaskRequest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    void 태스크의_내용을_수정한다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        Task task = TaskProvider.createTask(1L, project, null);
        UpdateTaskRequest updateTaskRequest = UpdateTaskRequest.builder()
                .title("new title")
                .managerId("25")
                .description("new description")
                .priority(4)
                .build();

        // when
        task.updateContents(updateTaskRequest);

        // then
        assertThat(task.getTitle()).isEqualTo(updateTaskRequest.getTitle());
        assertThat(task.getManagerId()).isEqualTo(Long.parseLong(updateTaskRequest.getManagerId()));
        assertThat(task.getDescription()).isEqualTo(updateTaskRequest.getDescription());
        assertThat(task.getPriority()).isEqualTo(updateTaskRequest.getPriority());
    }

}
