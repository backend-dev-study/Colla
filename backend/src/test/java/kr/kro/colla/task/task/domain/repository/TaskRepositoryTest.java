package kr.kro.colla.task.task.domain.repository;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.StoryProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.exception.exception.project.task_status.TaskStatusNotFoundException;
import kr.kro.colla.exception.exception.task.TaskNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.domain.repository.ProjectRepository;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.domain.repository.TaskStatusRepository;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.domain.repository.StoryRepository;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.TaskCntByStatus;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
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
    private StoryRepository storyRepository;

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
    void 스토리에_속한_태스크들을_조회한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(5L));
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("In Progress"));
        Story story = storyRepository.save(StoryProvider.createStory(project, "story title"));
        List<Task> taskList = taskRepository.saveAll(List.of(
                TaskProvider.createTaskWithTitleForRepository(null, project, story, taskStatus, "first task"),
                TaskProvider.createTaskWithTitleForRepository(null, project, story, taskStatus, "second task")
        ));

        // when
        List<Task> result = taskRepository.findStoryTasks(story);

        // then
        assertThat(result).hasSize(2);
        IntStream.range(0, result.size())
                .forEach(idx -> {
                    assertThat(result.get(idx).getTitle()).isEqualTo(taskList.get(idx).getTitle());
                    assertThat(result.get(idx).getStory().getTitle()).isEqualTo(story.getTitle());
                });
    }

    @Test
    void 프로젝트의_테스크들을_생성_날짜_오름차순으로_정렬해_조회한다() throws InterruptedException {
        // given
        int taskCount = 3;
        Project project = projectRepository.save(ProjectProvider.createProject(234234L));
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("new TaskStatus for Test"));
        for (int i = 0; i < taskCount; i++) {
            taskRepository.save(TaskProvider.createTaskForRepository(null, project, null, taskStatus));
            Thread.sleep(1);
        }

        // when
        List<Task> result = taskRepository.findAllOrderByCreatedAtAsc(project);

        // then
        assertThat(result.size()).isEqualTo(taskCount);
        IntStream.range(0, result.size() - 1)
                .forEach(idx -> assertThat(result.get(idx).getCreatedAt()).isBefore(result.get(idx + 1).getCreatedAt()));
    }

    @Test
    void 프로젝트의_테스크들을_생성_날짜_내림차순으로_정렬해_조회한다() throws InterruptedException {
        // given
        int taskCount = 3;
        Project project = projectRepository.save(ProjectProvider.createProject(234234L));
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("new TaskStatus for Test"));
        for (int i = 0; i < taskCount; i++) {
            taskRepository.save(TaskProvider.createTaskForRepository(null, project, null, taskStatus));
            Thread.sleep(1);
        }

        // when
        List<Task> result = taskRepository.findAllOrderByCreatedAtDesc(project);

        // then
        assertThat(result.size()).isEqualTo(taskCount);
        IntStream.range(0, result.size() - 1)
                .forEach(idx -> assertThat(result.get(idx).getCreatedAt()).isAfter(result.get(idx + 1).getCreatedAt()));
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
        Long managerId1 = 4325L, managerId2 = 2943L, justUserId = 23942L;
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("test-ing task repository"));
        Project project = projectRepository.save(ProjectProvider.createProject(justUserId));
        taskRepository.save(TaskProvider.createTaskForRepository(justUserId, project, null, taskStatus));
        taskRepository.save(TaskProvider.createTaskForRepository(managerId1, project, null, taskStatus));
        taskRepository.save(TaskProvider.createTaskForRepository(managerId2, project, null, taskStatus));
        List<Long> managers = List.of(managerId1, managerId2);

        // when
        List<Task> result = taskRepository.findAllFilterByManager(project, managers, false);

        // then
        assertThat(result.size()).isEqualTo(2);
        result.forEach(task -> {
            assertThat(managers).contains(task.getManagerId());
            assertThat(task.getProject().getId()).isEqualTo(project.getId());
        });
    }

    @Test
    void 프로젝트의_테스크들_중_담당자가_없는_테스크들도_필터링에_포함할_수_있다() {
        // given
        Long managerId = 234243L, userId = 239201L;
        Project project = projectRepository.save(ProjectProvider.createProject(userId));
        TaskStatus taskStatus1 = taskStatusRepository.save(new TaskStatus("test-ing for task not having manager"));
        TaskStatus taskStatus2 = taskStatusRepository.save(new TaskStatus("test-ing for null managerId"));
        taskRepository.save(TaskProvider.createTaskForRepository(null, project, null, taskStatus1));
        taskRepository.save(TaskProvider.createTaskForRepository(null, project, null, taskStatus2));
        taskRepository.save(TaskProvider.createTaskForRepository(managerId, project, null, taskStatus1));
        taskRepository.save(TaskProvider.createTaskForRepository(userId, project, null, taskStatus2));

        // when
        List<Task> result = taskRepository.findAllFilterByManager(project, Arrays.asList(managerId, null), true);

        // then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.stream()
                .filter(task -> task.getManagerId()==null)
                .count()).isEqualTo(2);
    }

    @Test
    void 검색한_키워드를_포함하는_제목을_가진_태스크를_조회한다() {
        // given
        String keyword = "dropdown";
        Project project = projectRepository.save(ProjectProvider.createProject(1L));
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("To Do"));
        taskRepository.save(TaskProvider.createTaskWithTitleForRepository(null, project, null, taskStatus, "implement filtering dropdown"));
        taskRepository.save(TaskProvider.createTaskWithTitleForRepository(null, project, null, taskStatus, "Decorate README.md"));
        taskRepository.save(TaskProvider.createTaskWithTitleForRepository(null, project, null, taskStatus, "refactor task dropdown"));

        // when
        List<Task> taskList = taskRepository.findTasksSearchByKeyword(project, keyword);

        // then
        assertThat(taskList).hasSize(2);
        taskList.forEach(task -> assertThat(task.getTitle()).contains(keyword));
    }

    @Test
    void 검색한_키워드를_포함하고_있는_태스크가_없다면_아무것도_반환하지_않는다() {
        // given
        String keyword = "api";
        Project project = projectRepository.save(ProjectProvider.createProject(1L));
        TaskStatus taskStatus = taskStatusRepository.save(new TaskStatus("To Do"));
        taskRepository.save(TaskProvider.createTaskWithTitleForRepository(null, project, null, taskStatus, "Set up CI/CD"));
        taskRepository.save(TaskProvider.createTaskWithTitleForRepository(null, project, null, taskStatus, "Write test code"));

        // when
        List<Task> taskList = taskRepository.findTasksSearchByKeyword(project, keyword);

        // then
        assertThat(taskList.size()).isZero();
    }

    @Test
    void 테스크_개수를_상태값_별_조회한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(234L));
        TaskStatus taskStatus0 = taskStatusRepository.save(new TaskStatus("그룹 바이는"));
        TaskStatus taskStatus1 = taskStatusRepository.save(new TaskStatus("또 기본 지원에"));
        TaskStatus taskStatus2 = taskStatusRepository.save(new TaskStatus("없는 알 수 없는 JPA 세계"));
        taskRepository.save(TaskProvider.createTaskForRepository(6243L, project, null, taskStatus0));
        taskRepository.save(TaskProvider.createTaskForRepository(6243L, project, null, taskStatus1));
        taskRepository.save(TaskProvider.createTaskForRepository(6243L, project, null, taskStatus1));
        taskRepository.save(TaskProvider.createTaskForRepository(6243L, project, null, taskStatus2));

        // when
        List<TaskCntByStatus> taskCntList = taskRepository.groupByTaskStatus(project);

        // then
        assertThat(taskCntList.size()).isEqualTo(3);
        assertThat(taskCntList.get(0).getTaskStatusName()).isEqualTo(taskStatus0.getName());
        assertThat(taskCntList.get(0).getTaskCnt()).isEqualTo(1);
        assertThat(taskCntList.get(0).getManager()).isNull();
        assertThat(taskCntList.get(1).getTaskStatusName()).isEqualTo(taskStatus1.getName());
        assertThat(taskCntList.get(1).getTaskCnt()).isEqualTo(2);
    }

    @Test
    void 테스크_개수를_상태값과_담당자_별_조회한다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(234L));
        TaskStatus taskStatus0 = taskStatusRepository.save(new TaskStatus("담당자, 상태별 조회시에는"));
        TaskStatus taskStatus1 = taskStatusRepository.save(new TaskStatus("존재하는 담당자/상태 조합만큼 개수!"));
        User user0 = userRepository.save(UserProvider.createUser());
        User user1 = userRepository.save(UserProvider.createUser2());
        taskRepository.save(TaskProvider.createTaskForRepository(user0.getId(), project, null, taskStatus0));
        taskRepository.save(TaskProvider.createTaskForRepository(user0.getId(), project, null, taskStatus1));
        taskRepository.save(TaskProvider.createTaskForRepository(user0.getId(), project, null, taskStatus1));
        taskRepository.save(TaskProvider.createTaskForRepository(user1.getId(), project, null, taskStatus1));

        // when
        List<TaskCntByStatus> taskCntList = taskRepository.groupByTaskStatusAndManager(project);

        // then
        assertThat(taskCntList.size()).isEqualTo(3);
        taskCntList.forEach(taskCnt -> {
            assertThat(List.of(user0.getName(), user1.getName())).contains(taskCnt.getManager());
            if (taskCnt.getManager().equals(user1.getName())) {
                assertThat(taskCnt.getTaskCnt()).isEqualTo(1);
                assertThat(taskCnt.getTaskStatusName()).isEqualTo(taskStatus1.getName());
            }
        });
    }

    @Test
    void 테스크_개수_조회시_담당자가_없으면_담당자_이름이_널_값이다() {
        // given
        Project project = projectRepository.save(ProjectProvider.createProject(234L));
        TaskStatus taskStatus0 = taskStatusRepository.save(new TaskStatus("담당자, 상태별 조회시에는"));
        TaskStatus taskStatus1 = taskStatusRepository.save(new TaskStatus("담당자 없는 테스크 있을 수 있음 주의!"));
        taskRepository.save(TaskProvider.createTaskForRepository(null, project, null, taskStatus0));
        taskRepository.save(TaskProvider.createTaskForRepository(null, project, null, taskStatus1));

        // when
        List<TaskCntByStatus> taskCntList = taskRepository.groupByTaskStatusAndManager(project);

        // then
        assertThat(taskCntList.size()).isEqualTo(2);
        assertThat(taskCntList.get(0).getManager()).isNull();
        assertThat(taskCntList.get(0).getTaskCnt()).isEqualTo(1);
        assertThat(taskCntList.get(0).getTaskStatusName()).isEqualTo(taskStatus0.getName());
    }

}
