package kr.kro.colla.task.task.domain.repository;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.exception.exception.project.task_status.TaskStatusNotFoundException;
import kr.kro.colla.exception.exception.task.TaskNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.domain.repository.TaskStatusRepository;
import kr.kro.colla.task.task.domain.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Test
    void 프로젝트에_새로운_태스크를_등록한다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        projectRepository.save(project);

        TaskStatus taskStatus = taskStatusRepository.findByName("To Do")
                .orElseThrow(TaskStatusNotFoundException::new);
        Task task = TaskProvider.createTaskForRepository(null, project, null, taskStatus, 3);

        // when
        Task result = taskRepository.save(task);

        // then
        assertThat(result.getTitle()).isEqualTo(task.getTitle());
        assertThat(result.getTaskStatus().getName()).isEqualTo("To Do");
        assertThat(result.getProject().getName()).isEqualTo(project.getName());
    }

    @Test
    void 프로젝트의_태스크를_조회한다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        projectRepository.save(project);

        Task task = TaskProvider.createTaskForRepository(null, project, null, null, 3);
        taskRepository.save(task);

        // when
        Task result = taskRepository.findById(task.getId())
                .orElseThrow(TaskNotFoundException::new);

        // then
        assertThat(result.getTitle()).isEqualTo(task.getTitle());
        assertThat(result.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    void 존재하지_않는_태스크를_조회할_경우_예외가_발생한다() {
        // when, then
        assertThatThrownBy(
                () -> taskRepository.findById(100L)
                        .orElseThrow(TaskNotFoundException::new)
        ).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void 태스크의_상태값을_다른_상태값으로_변경한다() {
        // given
        Project project = ProjectProvider.createProject(345L);
        projectRepository.save(project);

        TaskStatus taskStatus1 = taskStatusRepository.save(new TaskStatus("before"));
        TaskStatus taskStatus2 = taskStatusRepository.save(new TaskStatus("after"));;

        Task task1 = TaskProvider.createTaskForRepository(null, project, null, taskStatus1, 3);
        Task task2 = TaskProvider.createTaskForRepository(null, project, null, taskStatus1, 3);
        taskRepository.save(task1);
        taskRepository.save(task2);

        // when
        taskRepository.bulkUpdateTaskStatusToAnother(taskStatus1, taskStatus2);

        // then
        Task result1 = taskRepository.findById(task1.getId()).get();
        assertThat(result1.getTaskStatus().getName()).isEqualTo(taskStatus2.getName());
        assertThat(result1.getTaskStatus().getId()).isEqualTo(taskStatus2.getId());
        Task result2 = taskRepository.findById(task2.getId()).get();
        assertThat(result2.getTaskStatus().getName()).isEqualTo(taskStatus2.getName());
        assertThat(result2.getTaskStatus().getId()).isEqualTo(taskStatus2.getId());
    }

    @Test
    void 프로젝트의_태스크를_중요도순_으로_정렬하여_조회한다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        projectRepository.save(project);

        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("backend"));

        Task task1 = TaskProvider.createTaskForRepository(null, project, null, taskStatus, 4);
        Task task2 = TaskProvider.createTaskForRepository(null, project, null, taskStatus, 2);
        taskRepository.save(task1);
        taskRepository.save(task2);

        // when
        List<Task> taskList = taskRepository.findAllOrderByPriority(project);

        // then
        assertThat(taskList.size()).isEqualTo(2);
        assertThat(taskList.get(0).getTitle()).isEqualTo(task2.getTitle());
        assertThat(taskList.get(1).getTitle()).isEqualTo(task1.getTitle());
    }

}
