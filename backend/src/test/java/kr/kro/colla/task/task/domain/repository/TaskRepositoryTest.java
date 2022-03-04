package kr.kro.colla.task.task.domain.repository;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.exception.exception.project.task_status.TaskStatusNotFoundException;
import kr.kro.colla.exception.exception.task.TaskNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.domain.repository.TaskStatusRepository;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@EnableJpaAuditing
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 프로젝트에_새로운_태스크를_등록한다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        projectRepository.save(project);

        TaskStatus taskStatus = taskStatusRepository.findByName("To Do")
                .orElseThrow(TaskStatusNotFoundException::new);
        Task task = TaskProvider.createTaskForRepository(null, project, null, taskStatus);

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

        Task task = TaskProvider.createTaskForRepository(null, project, null, null);
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

        Task task1 = TaskProvider.createTaskForRepository(null, project, null, taskStatus1);
        Task task2 = TaskProvider.createTaskForRepository(null, project, null, taskStatus1);
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
    void 프로젝트의_테스크들을_생성_날짜_오름차순으로_정렬해_조회한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(234234L));
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("new TaskStatus for Test"));
        List<Task> taskList = taskRepository.saveAll(List.of(
                TaskProvider.createTaskForRepository(null, project, null, taskStatus),
                TaskProvider.createTaskForRepository(null, project, null, taskStatus),
                TaskProvider.createTaskForRepository(null, project, null, taskStatus)
        ));

        // when
        List<Task> result = taskRepository.findAllOrderByCreatedAtAsc(project);

        // then
        assertThat(result.size()).isEqualTo(taskList.size());
        IntStream.range(0, result.size())
                .filter(idx -> idx != 0)
                .forEach(idx -> assertThat(result.get(idx).getCreatedAt().isBefore(result.get(idx - 1).getCreatedAt())));
    }

    @Test
    void 프로젝트의_테스크들을_생성_날짜_내림차순으로_정렬해_조회한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(234234L));
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("new TaskStatus for Test"));
        List<Task> taskList = taskRepository.saveAll(List.of(
                TaskProvider.createTaskForRepository(null, project, null, taskStatus),
                TaskProvider.createTaskForRepository(null, project, null, taskStatus),
                TaskProvider.createTaskForRepository(null, project, null, taskStatus)
        ));

        // when
        List<Task> result = taskRepository.findAllOrderByCreatedAtDesc(project);

        // then
        assertThat(result.size()).isEqualTo(taskList.size());
        IntStream.range(0, result.size())
                .filter(idx -> idx != 0)
                .forEach(idx -> assertThat(result.get(idx).getCreatedAt().isAfter(result.get(idx - 1).getCreatedAt())));
    }

    @Test
    void 프로젝트의_태스크를_우선순위_오름차순으로_조회한다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        projectRepository.save(project);

        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("To Do"));
        List<Task> taskList = taskRepository.saveAll(List.of(
                TaskProvider.createTaskWithPriorityForRepository(null, project, null, taskStatus, 4),
                TaskProvider.createTaskWithPriorityForRepository(null, project, null, taskStatus, 2),
                TaskProvider.createTaskWithPriorityForRepository(null, project, null, taskStatus, 1)
        ));

        // when
        List<Task> result = taskRepository.findAllOrderByPriorityAsc(project);

        // then
        assertThat(result.size()).isEqualTo(taskList.size());
        IntStream.range(0, result.size() - 1)
                .forEach(idx -> assertThat(result.get(idx).getPriority()).isLessThan(result.get(idx + 1).getPriority()));
    }

    @Test
    void 프로젝트의_태스크를_우선순위_내림차순으로_조회한다() {
        // given
        Project project = ProjectProvider.createProject(1L);
        projectRepository.save(project);

        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("To Do"));

        List<Task> taskList = taskRepository.saveAll(List.of(
                TaskProvider.createTaskWithPriorityForRepository(null, project, null, taskStatus, 4),
                TaskProvider.createTaskWithPriorityForRepository(null, project, null, taskStatus, 2),
                TaskProvider.createTaskWithPriorityForRepository(null, project, null, taskStatus, 1)
        ));

        // when
        List<Task> result = taskRepository.findAllOrderByPriorityDesc(project);

        // then
        assertThat(result.size()).isEqualTo(taskList.size());
        IntStream.range(0, result.size() - 1)
                .forEach(idx -> assertThat(result.get(idx).getPriority()).isGreaterThan(result.get(idx + 1).getPriority()));
    }

    @Test
    void 프로젝트의_테스크를_상태값들로_필터링해서_조회한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(4253123L));
        User user = userRepository.save(UserProvider.createUser2());
        List<TaskStatus> filterStatus = List.of(
                taskStatusRepository.save(new TaskStatus("Status To Filter1")),
                taskStatusRepository.save(new TaskStatus("Status To Filter2"))
        );

        TaskStatus notFilterStatus = taskStatusRepository.save(new TaskStatus("Status Not Wanted"));
        List<Task> tasks = List.of(
                taskRepository.save(TaskProvider.createTaskForRepository(user.getId(), project, null, filterStatus.get(0))),
                taskRepository.save(TaskProvider.createTaskForRepository(user.getId(), project, null, filterStatus.get(1))),
                taskRepository.save(TaskProvider.createTaskForRepository(user.getId(), project, null, filterStatus.get(1)))
        );
        taskRepository.save(TaskProvider.createTaskForRepository(user.getId(), project, null, notFilterStatus));

        List<String> statuses = filterStatus.stream()
                .map(taskStatus -> taskStatus.getName())
                .collect(Collectors.toList());

        // when
        List<Task> result = taskRepository.findAllFilterByTaskStatus(project, statuses);

        // then
        assertThat(result.size()).isEqualTo(tasks.size());
        assertThat(result
                .stream()
                .filter(task -> !statuses.contains(task.getTaskStatus().getName())).count()).isEqualTo(0);
    }

    @Test
    void 해당하는_상태값의_테스크가_없을시_빈_배열이_반환된다() {
        Project project = projectRepository.save(ProjectProvider.createProject(4253123L));
        User user = userRepository.save(UserProvider.createUser2());
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("Status Not Wanted"));
        taskRepository.save(TaskProvider.createTaskForRepository(user.getId(), project, null, taskStatus));

        // when
        List<Task> result = taskRepository.findAllFilterByTaskStatus(project, List.of("Not", "Exist", "Status"));

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    void 프로젝트의_테스크들을_담당자로_필터링해서_조회한다() {
        // given
        Long targetManagerId = 4325L, otherManagerId = 2943L;
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("test-ing task repository"));
        Project project = projectRepository.save(ProjectProvider.createProject(243920L));
        taskRepository.save(TaskProvider.createTaskForRepository(targetManagerId, project, null, taskStatus));
        taskRepository.save(TaskProvider.createTaskForRepository(otherManagerId, project, null, taskStatus));
        taskRepository.save(TaskProvider.createTaskForRepository(targetManagerId, project, null, taskStatus));

        // when
        List<Task> result = taskRepository.findAllFilterByManager(project, targetManagerId);

        // then
        assertThat(result.size()).isEqualTo(2);
        result.forEach(task -> {
            assertThat(task.getManagerId().equals(targetManagerId));
            assertThat(task.getProject().getId().equals(project.getId()));
        });
    }

    @Test
    void 프로젝트의_테스크들_중_담당자가_없는_테스크들을_필터링해서_조회한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(234234L));
        TaskStatus taskStatus1 = taskStatusRepository.save(new TaskStatus("test-ing for task not having manager"));
        TaskStatus taskStatus2 = taskStatusRepository.save(new TaskStatus("test-ing for null managerId"));
        taskRepository.save(TaskProvider.createTaskForRepository(null, project, null, taskStatus1));
        taskRepository.save(TaskProvider.createTaskForRepository(null, project, null, taskStatus2));
        taskRepository.save(TaskProvider.createTaskForRepository(23456L, project, null, taskStatus1));

        // when
        List<Task> result = taskRepository.findAllFilterByManager(project, null);

        // then
        assertThat(result.size()).isEqualTo(2);
        result.forEach(task -> assertThat(task.getManagerId()).isNull());
    }
}
