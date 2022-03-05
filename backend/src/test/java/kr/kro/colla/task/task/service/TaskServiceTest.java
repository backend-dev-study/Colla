package kr.kro.colla.task.task.service;

import kr.kro.colla.common.fixture.*;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.service.TaskStatusService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.service.StoryService;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import kr.kro.colla.task.task.presentation.dto.*;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import kr.kro.colla.task.task_tag.service.TaskTagService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private StoryService storyService;

    @Mock
    private ProjectService projectService;

    @Mock
    private TaskTagService taskTagService;

    @Mock
    private TaskStatusService taskStatusService;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    void 프로젝트에_새로운_태스크를_생성한다() {
        // given
        String storyTitle = "user can login with github", taskStatusName = "To Do";
        Project project = ProjectProvider.createProject(1L);
        Story story = StoryProvider.createStory(project, storyTitle);

        CreateTaskRequest createTaskRequest = new CreateTaskRequest("task title", null, "task description", 4, taskStatusName, "[\"backend\"]", 1L, storyTitle, "[]");
        TaskStatus taskStatus = new TaskStatus(taskStatusName);
        Task task = Task.builder()
                .title(createTaskRequest.getTitle())
                .managerId(createTaskRequest.getManagerId())
                .description(createTaskRequest.getDescription())
                .priority(createTaskRequest.getPriority())
                .project(project)
                .taskStatus(taskStatus)
                .story(story)
                .preTasks(createTaskRequest.getPreTasks())
                .build();
        ReflectionTestUtils.setField(task, "id", 1L);

        given(projectService.findProjectById(1L))
                .willReturn(project);
        given(storyService.findStoryByTitle(storyTitle))
                .willReturn(story);
        given(taskStatusService.findTaskStatusByName(taskStatusName))
                .willReturn(taskStatus);
        given(taskTagService.translateTaskTags(any(Task.class), eq(createTaskRequest.getTags())))
                .willReturn(List.of(new TaskTag(task, new Tag("backend"))));
        given(taskRepository.save(any(Task.class)))
                .willReturn(task);

        // when
        Long taskId = taskService.createTask(createTaskRequest);

        // then
        assertThat(taskId).isNotNull();
        verify(projectService, times(1)).findProjectById(1L);
        verify(storyService, times(1)).findStoryByTitle(storyTitle);
        verify(taskStatusService, times(1)).findTaskStatusByName(taskStatusName);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void 프로젝트의_태스크를_조회한다() {
        // given
        Long taskId = 1L, managerId = 5L;
        User manager = UserProvider.createUser();
        ReflectionTestUtils.setField(manager, "id", managerId);

        Project project = ProjectProvider.createProject(manager.getId());
        Story story = StoryProvider.createStory(project, "user can login with github");
        Task task = TaskProvider.createTask(manager.getId(), project, story);
        ReflectionTestUtils.setField(task, "id", taskId);

        given(taskRepository.findById(eq(taskId)))
                .willReturn(Optional.of(task));
        given(userService.findUserById(eq(managerId)))
                .willReturn(manager);

        // when
        ProjectTaskResponse projectTaskResponse = taskService.getTask(taskId);

        // then
        assertThat(projectTaskResponse.getId()).isEqualTo(taskId);
        assertThat(projectTaskResponse.getTitle()).isEqualTo(task.getTitle());
        assertThat(projectTaskResponse.getStory()).isEqualTo(story.getTitle());
        assertThat(projectTaskResponse.getManager()).isEqualTo(manager.getName());
        verify(taskRepository, times(1)).findById(anyLong());
        verify(userService, times(1)).findUserById(anyLong());
    }

    @Test
    void 담당자와_스토리가_없는_태스크를_조회한다() {
        // given
        Long taskId = 1L;
        Project project = ProjectProvider.createProject(1L);
        Task task = TaskProvider.createTask(null, project, null);
        ReflectionTestUtils.setField(task, "id", taskId);

        given(taskRepository.findById(eq(taskId)))
                .willReturn(Optional.of(task));

        // when
        ProjectTaskResponse projectTaskResponse = taskService.getTask(taskId);

        // then
        assertThat(projectTaskResponse.getManager()).isNull();
        assertThat(projectTaskResponse.getStory()).isNull();
    }

    @Test
    void 태스크의_내용을_수정한다() {
        // given
        Long taskId = 1L;
        User user = UserProvider.createUser();
        ReflectionTestUtils.setField(user, "id", 1L);

        Project project = ProjectProvider.createProject(user.getId());
        Story story = StoryProvider.createStory(project, "story title");
        Task task = TaskProvider.createTask(user.getId(), project, story);
        ReflectionTestUtils.setField(task, "id", taskId);
        task.addTags(List.of(
                new TaskTag(task, new Tag("backend")),
                new TaskTag(task, new Tag("frontend"))
        ));

        UpdateTaskRequest updateTaskRequest = UpdateTaskRequest.builder()
                .title("new title")
                .managerId("25")
                .description("new description")
                .story("new story")
                .priority(4)
                .tags("[\"backend\", \"frontend\", \"bug fix\"]")
                .build();
        List<TaskTag> taskTags = List.of(
                new TaskTag(task, new Tag("refactoring")),
                new TaskTag(task, new Tag("backend")),
                new TaskTag(task, new Tag("bug fix"))
        );
        Story newStory = StoryProvider.createStory(project, updateTaskRequest.getStory());

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));
        given(taskTagService.translateTaskTags(any(Task.class), eq(updateTaskRequest.getTags())))
                .willReturn(taskTags);
        given(storyService.findStoryByTitle(eq(updateTaskRequest.getStory())))
                .willReturn(newStory);

        // when
        taskService.updateTask(taskId, updateTaskRequest);

        // then
        assertThat(task.getTitle()).isEqualTo(updateTaskRequest.getTitle());
        assertThat(task.getDescription()).isEqualTo(updateTaskRequest.getDescription());
        assertThat(task.getStory().getTitle()).isEqualTo(newStory.getTitle());
        assertThat(task.getManagerId()).isNotEqualTo(taskId);
        assertThat(task.getPriority()).isEqualTo(updateTaskRequest.getPriority());
        assertThat(task.getTaskTags()).hasSize(3);
        assertThat(task.getTaskTags()).extracting(taskTag -> taskTag.getTag().getName())
                .containsExactly("backend", "refactoring", "bug fix");
    }

    @Test
    void 스토리에_속해있지_않은_태스크에_스토리를_추가한다() {
        // given
        Long userId = 1L, taskId = 5L;
        User user = UserProvider.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        Project project = ProjectProvider.createProject(user.getId());
        Task task = TaskProvider.createTask(null, project, null);
        ReflectionTestUtils.setField(task, "id", taskId);

        UpdateTaskRequest updateTaskRequest = UpdateTaskRequest.builder()
                .title(task.getTitle())
                .managerId("")
                .description(task.getDescription())
                .story("new story")
                .priority(task.getPriority())
                .build();
        Story newStory = StoryProvider.createStory(project, updateTaskRequest.getStory());

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));
        given(storyService.findStoryByTitle(eq(updateTaskRequest.getStory())))
                .willReturn(newStory);

        // when
        taskService.updateTask(taskId, updateTaskRequest);

        // then
        assertThat(task.getStory()).isNotNull();
        assertThat(task.getStory().getTitle()).isEqualTo(updateTaskRequest.getStory());
        verify(storyService, times(1)).findStoryByTitle(eq(updateTaskRequest.getStory()));
    }

    @Test
    void 기존에_특정_스토리에_속해있던_태스크의_스토리를_수정한다() {
        // given
        Long userId = 1L, taskId = 5L;
        User user = UserProvider.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        Project project = ProjectProvider.createProject(user.getId());
        Story oldStory = StoryProvider.createStory(project, "old story");
        Task task = TaskProvider.createTask(null, project, oldStory);
        ReflectionTestUtils.setField(task, "id", taskId);

        UpdateTaskRequest updateTaskRequest = UpdateTaskRequest.builder()
                .title(task.getTitle())
                .managerId("")
                .description(task.getDescription())
                .story("new story")
                .priority(task.getPriority())
                .build();
        Story newStory = StoryProvider.createStory(project, updateTaskRequest.getStory());

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));
        given(storyService.findStoryByTitle(eq(updateTaskRequest.getStory())))
                .willReturn(newStory);

        // when
        taskService.updateTask(taskId, updateTaskRequest);

        // then
        assertThat(task.getStory()).isNotNull();
        assertThat(task.getStory().getTitle()).isNotEqualTo(oldStory.getTitle());
        assertThat(task.getStory().getTitle()).isEqualTo(newStory.getTitle());
        verify(storyService, times(1)).findStoryByTitle(eq(updateTaskRequest.getStory()));
    }

    @Test
    void 기존_스토리와_동일한_스토리로_수정_시_반영되지_않는다() {
        // given
        Long userId = 1L, taskId = 5L;
        User user = UserProvider.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        Project project = ProjectProvider.createProject(user.getId());
        Story oldStory = StoryProvider.createStory(project, "old story");
        Task task = TaskProvider.createTask(null, project, oldStory);
        ReflectionTestUtils.setField(task, "id", taskId);

        UpdateTaskRequest updateTaskRequest = UpdateTaskRequest.builder()
                .title(task.getTitle())
                .managerId("")
                .description(task.getDescription())
                .story("old story")
                .priority(task.getPriority())
                .build();

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));

        // when
        taskService.updateTask(taskId, updateTaskRequest);

        // then
        verify(storyService, never()).findStoryByTitle(eq(updateTaskRequest.getStory()));
    }

    @Test
    void 태스크가_스토리에_속해있지_않고_추가하지도_않았다면_아무런_반영도_되지_않는다() {
        // given
        Long userId = 1L, taskId = 5L;
        User user = UserProvider.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        Project project = ProjectProvider.createProject(user.getId());
        Task task = TaskProvider.createTask(null, project, null);
        ReflectionTestUtils.setField(task, "id", taskId);

        UpdateTaskRequest updateTaskRequest = UpdateTaskRequest.builder()
                .title(task.getTitle())
                .managerId("")
                .description(task.getDescription())
                .story("")
                .priority(task.getPriority())
                .build();

        given(taskRepository.findById(eq(taskId)))
                .willReturn(Optional.of(task));

        // when
        taskService.updateTask(taskId, updateTaskRequest);

        // then
        verify(storyService, never()).findStoryByTitle(eq(updateTaskRequest.getStory()));
    }

    @Test
    void 삭제될_테스크_상태값은_하위_테스크들이_변경된_후_삭제된다() {
        // given
        Long projectId = 134214L;
        String beforeStr = "IM_BEFORE_STATUS", afterStr = "IM_AFTER_STATUS";
        TaskStatus before = new TaskStatus(beforeStr);
        TaskStatus after = new TaskStatus(afterStr);

        Project project = ProjectProvider.createProject(987987L);
        project.addStatus(before);
        project.addStatus(after);

        given(taskStatusService.findTaskStatusByName(beforeStr))
                .willReturn(before);
        given(taskStatusService.findTaskStatusByName(afterStr))
                .willReturn(after);
        given(projectService.findProjectById(projectId))
                .willReturn(project);

        // when
        taskService.deleteTaskStatus(projectId, beforeStr, afterStr);

        // then
        assertThat(project.getTaskStatuses().size()).isEqualTo(1);
        assertThat(project.getTaskStatuses().get(0).getName()).isEqualTo(afterStr);
        verify(taskStatusService, times(2)).findTaskStatusByName(anyString());
        verify(taskRepository, times(1)).bulkUpdateTaskStatusToAnother(any(TaskStatus.class), any(TaskStatus.class));
    }

    @Test
    void 테스크의_상태값을_수정한다() {
        // given
        Long taskId = 482593L;
        TaskStatus before = new TaskStatus("기존_상태값");
        TaskStatus after = new TaskStatus("변경_후_새로운_상태값");
        Task task = TaskProvider.createTaskForRepository(2345L, null, null, before);

        given(taskRepository.findById(taskId))
                .willReturn(Optional.of(task));
        given(taskStatusService.findTaskStatusByName(after.getName()))
                .willReturn(after);

        // when
        taskService.updateTaskStatus(taskId, after.getName());

        // then
        assertThat(task.getTaskStatus().getName()).isEqualTo(after.getName());
    }

    @Test
    void 프로젝트의_테스크들을_생성_날짜_오름차순으로_조회한다() {
        // given
        Long projectId = 132128L, managerId = 9283L;
        Project project = ProjectProvider.createProject(120394L);
        User user = UserProvider.createUser2();
        List<Task> tasks = List.of(
                TaskProvider.createTask(managerId, project,null),
                TaskProvider.createTask(null, project,null)
        );

        given(projectService.initializeProjectInfo(projectId))
                .willReturn(project);
        given(taskRepository.findAllOrderByCreatedAtAsc(any(Project.class)))
                .willReturn(tasks);
        given(userService.findUserById(managerId))
                .willReturn(user);

        // when
        List<ProjectTaskSimpleResponse> result = taskService.getTasksOrderByCreatedDate(projectId, true);

        // then
        assertThat(result.size()).isEqualTo(tasks.size());
        List<String> names = result
                .stream()
                .map(response -> response.getManagerName())
                .collect(Collectors.toList());

        assertThat(names).containsExactlyInAnyOrder(user.getName(), null);
        verify(taskRepository, times(1)).findAllOrderByCreatedAtAsc(any());
        verify(taskRepository, times(0)).findAllOrderByCreatedAtDesc(any());
    }

    @Test
    void 프로젝트의_테스크들을_생성_날짜_내림차순으로_조회한다() {
        // given
        Long projectId = 132128L, managerId = 9283L;
        Project project = ProjectProvider.createProject(120394L);
        User user = UserProvider.createUser2();
        List<Task> tasks = List.of(
                TaskProvider.createTask(managerId, project, null),
                TaskProvider.createTask(null, project, null)
        );

        given(projectService.initializeProjectInfo(projectId))
                .willReturn(project);
        given(taskRepository.findAllOrderByCreatedAtDesc(any(Project.class)))
                .willReturn(tasks);
        given(userService.findUserById(managerId))
                .willReturn(user);

        // when
        List<ProjectTaskSimpleResponse> result = taskService.getTasksOrderByCreatedDate(projectId, false);

        // then
        assertThat(result.size()).isEqualTo(tasks.size());
        List<String> names = result
                .stream()
                .map(response -> response.getManagerName())
                .collect(Collectors.toList());

        assertThat(names).containsExactlyInAnyOrder(user.getName(), null);
        verify(taskRepository, times(0)).findAllOrderByCreatedAtAsc(any());
        verify(taskRepository, times(1)).findAllOrderByCreatedAtDesc(any());
    }

    @Test
    void 프로젝트의_태스크들을_우선순위_오름차순으로_조회한다() {
        // given
        Long projectId = 1L, memberId = 5L;
        User member = UserProvider.createUser();
        Project project = ProjectProvider.createProject(memberId);

        Task task1 = TaskProvider.createTaskWithPriority(memberId, project, null, 3);
        Task task2 = TaskProvider.createTaskWithPriority(memberId, project, null, 1);

        given(projectService.initializeProjectInfo(eq(projectId)))
                .willReturn(project);
        given(taskRepository.findAllOrderByPriorityAsc(any(Project.class)))
                .willReturn(List.of(task2, task1));
        given(userService.findUserById(eq(memberId)))
                .willReturn(member);

        // when
        List<ProjectTaskSimpleResponse> taskList = taskService.getTasksOrderByPriority(projectId, true);

        // then
        assertThat(taskList.size()).isEqualTo(2);
        assertThat(taskList.get(0).getTitle()).isEqualTo(task2.getTitle());
        assertThat(taskList.get(1).getTitle()).isEqualTo(task1.getTitle());
        assertThat(taskList.get(0).getPriority()).isLessThan(taskList.get(1).getPriority());
        verify(taskRepository, never()).findAllOrderByPriorityDesc(any(Project.class));
        verify(taskRepository, times(1)).findAllOrderByPriorityAsc(any(Project.class));
    }

    @Test
    void 프로젝트의_태스크들을_우선순위_내림차순으로_조회한다() {
        // given
        Long projectId = 1L, memberId = 5L;
        User member = UserProvider.createUser();
        Project project = ProjectProvider.createProject(memberId);

        Task task1 = TaskProvider.createTaskWithPriority(memberId, project, null, 5);
        Task task2 = TaskProvider.createTaskWithPriority(memberId, project, null, 3);

        given(projectService.initializeProjectInfo(eq(projectId)))
                .willReturn(project);
        given(taskRepository.findAllOrderByPriorityDesc(any(Project.class)))
                .willReturn(List.of(task1, task2));
        given(userService.findUserById(eq(memberId)))
                .willReturn(member);

        // when
        List<ProjectTaskSimpleResponse> taskList = taskService.getTasksOrderByPriority(projectId, false);

        // then
        assertThat(taskList.size()).isEqualTo(2);
        assertThat(taskList.get(0).getTitle()).isEqualTo(task1.getTitle());
        assertThat(taskList.get(1).getTitle()).isEqualTo(task2.getTitle());
        assertThat(taskList.get(0).getPriority()).isGreaterThan(taskList.get(1).getPriority());
        verify(taskRepository, never()).findAllOrderByPriorityAsc(any(Project.class));
        verify(taskRepository, times(1)).findAllOrderByPriorityDesc(any(Project.class));
    }

    @Test
    void 프로젝트의_테스크들을_상태값들로_필터링해_조회한다() {
        // given
        Long projectId = 25234L, managerId = 6345L;
        Project project = ProjectProvider.createProject(24525L);
        User user = UserProvider.createUser2();
        List<TaskStatus> taskStatusList = List.of(
            new TaskStatus("**task status to filter**"),
            new TaskStatus("**multiple status can be selected**")
        );
        List<Task> taskList = List.of(
            TaskProvider.createTaskForRepository(managerId, project, null, taskStatusList.get(0)),
            TaskProvider.createTaskForRepository(managerId, project, null, taskStatusList.get(1))
        );
        List<String> statuses = taskStatusList.stream()
                .map(taskStatus -> taskStatus.getName())
                .collect(Collectors.toList());

        given(projectService.initializeProjectInfo(projectId))
                .willReturn(project);
        given(taskRepository.findAllFilterByTaskStatus(any(Project.class), any(List.class)))
                .willReturn(taskList);
        given(userService.findUserById(managerId))
                .willReturn(user);

        // when
        List<ProjectTaskSimpleResponse> result = taskService.getTasksFilterByStatus(projectId, List.of());

        // then
        assertThat(result.size()).isEqualTo(taskList.size());
        result.forEach(response -> {
                assertThat(response.getManagerName()).isEqualTo(user.getName());
                assertThat(statuses.contains(response.getStatus()));
        });
    }

    @Test
    void 태그로_필터링_시_선택한_태그들을_포함하는_태스크들만_조회한다() {
        // given
        Long projectId = 1L, memberId = 5L;
        Project project = ProjectProvider.createProject(memberId);
        List<Tag> tags = List.of(
                TagProvider.createTag("backend"),
                TagProvider.createTag("frontend"),
                TagProvider.createTag("refactoring"),
                TagProvider.createTag("bug fix")
        );

        Task task1 = TaskProvider.createTask(memberId, project, null);
        task1.addTags(List.of(
                TaskTagProvider.createTaskTag(task1, tags.get(0)),
                TaskTagProvider.createTaskTag(task1, tags.get(2)),
                TaskTagProvider.createTaskTag(task1, tags.get(3))
        ));
        Task task2 = TaskProvider.createTask(memberId, project, null);
        task2.addTags(List.of(
                TaskTagProvider.createTaskTag(task2, tags.get(1)),
                TaskTagProvider.createTaskTag(task2, tags.get(2))
        ));
        Task task3 = TaskProvider.createTask(memberId, project, null);
        task3.addTags(List.of(
                TaskTagProvider.createTaskTag(task3, tags.get(0)),
                TaskTagProvider.createTaskTag(task3, tags.get(1)),
                TaskTagProvider.createTaskTag(task3, tags.get(2))
        ));

        given(projectService.initializeProjectInfo(eq(projectId)))
                .willReturn(project);
        given(taskRepository.findAllOrderByCreatedAtDesc(any(Project.class)))
                .willReturn(List.of(task1, task2, task3));

        // when
        List<ProjectTaskSimpleResponse> taskList = taskService.getTasksFilterByTags(projectId, new ArrayList<>(List.of("refactoring", "backend")));

        // then
        assertThat(taskList.size()).isEqualTo(2);
        assertThat(taskList.get(0).getTags()).contains("refactoring", "backend");
        assertThat(taskList.get(1).getTags()).contains("refactoring", "backend");
    }

    @Test
    void 특정_태그들을_가진_태스크가_없다면_필터링된_결과도_없다() {
        // given
        Long projectId = 1L, memberId = 5L;
        Project project = ProjectProvider.createProject(memberId);
        List<Tag> tags = List.of(
                TagProvider.createTag("enhancement"),
                TagProvider.createTag("bug fix"),
                TagProvider.createTag("documentation")
        );

        Task task1 = TaskProvider.createTask(memberId, project, null);
        task1.addTags(List.of(TaskTagProvider.createTaskTag(task1, tags.get(0))));
        Task task2 = TaskProvider.createTask(memberId, project, null);
        task2.addTags(List.of(TaskTagProvider.createTaskTag(task2, tags.get(1))));

        given(projectService.initializeProjectInfo(eq(projectId)))
                .willReturn(project);
        given(taskRepository.findAllOrderByCreatedAtDesc(any(Project.class)))
                .willReturn(List.of(task1, task2));

        // when
        List<ProjectTaskSimpleResponse> taskList = taskService.getTasksFilterByTags(projectId, new ArrayList<>(List.of("documentation")));

        // then
        assertThat(taskList.size()).isZero();
    }

    @Test
    void 프로젝트의_태스크들을_스토리로_그룹핑한다() {
        // given
        Long projectId = 1L, memberId = 5L;
        Project project = ProjectProvider.createProject(memberId);
        Story story = StoryProvider.createStory(project, "user can login with github");
        ReflectionTestUtils.setField(project, "stories", List.of(story));

        Task task1 = TaskProvider.createTask(memberId, project, story);
        Task task2 = TaskProvider.createTask(memberId, project, null);
        Task task3 = TaskProvider.createTask(memberId, project, story);

        given(projectService.initializeProjectInfo(eq(projectId)))
                .willReturn(project);
        given(taskRepository.findAllOrderByCreatedAtDesc(any(Project.class)))
                .willReturn(List.of(task1, task2, task3));

        // when
        List<ProjectStoryTaskResponse> storyTaskList = taskService.getTasksGroupByStory(projectId);

        // then
        assertThat(storyTaskList).hasSize(2);
        assertThat(storyTaskList.get(0).getStory()).isEqualTo(story.getTitle());
        assertThat(storyTaskList.get(0).getTaskList()).hasSize(2);
        assertThat(storyTaskList.get(1).getStory()).isNull();
        assertThat(storyTaskList.get(1).getTaskList()).hasSize(1);
    }

    @Test
    void 프로젝트에_아무런_스토리가_없다면_모든_태스크는_스토리가_없는_그룹으로_묶인다() {
        // given
        Long projectId = 1L, memberId = 5L;
        Project project = ProjectProvider.createProject(memberId);

        Task task1 = TaskProvider.createTask(memberId, project, null);
        Task task2 = TaskProvider.createTask(memberId, project, null);

        given(projectService.initializeProjectInfo(eq(projectId)))
                .willReturn(project);
        given(taskRepository.findAllOrderByCreatedAtDesc(any(Project.class)))
                .willReturn(List.of(task1, task2));

        // when
        List<ProjectStoryTaskResponse> storyTaskList = taskService.getTasksGroupByStory(projectId);

        // then
        assertThat(storyTaskList).hasSize(1);
        assertThat(storyTaskList.get(0).getStory()).isNull();
        assertThat(storyTaskList.get(0).getTaskList()).hasSize(2);
    }

    @Test
    void 프로젝트의_테스크들을_담당자에_따라_필터링해_조회한다() {
        // given
        Long projectId = 3242L, managerId1 = 6933L, managerId2 = 5263L;
        Project project = ProjectProvider.createProject(25332L);
        User user = UserProvider.createUser2();
        List<Task> tasks = List.of(
                TaskProvider.createTask(managerId1, project, null),
                TaskProvider.createTask(managerId1, project, null),
                TaskProvider.createTask(managerId2, project, null)
        );

        given(projectService.initializeProjectInfo(projectId))
                .willReturn(project);
        given(taskRepository.findAllFilterByManager(any(Project.class), anyList(), eq(false)))
                .willReturn(tasks);
        given(userService.findUserById(managerId1))
                .willReturn(user);
        given(userService.findUserById(managerId2))
                .willReturn(user);

        // when
        List<ProjectTaskSimpleResponse> result = taskService.getTasksFilterByManager(projectId, List.of(managerId1, managerId2), false);

        // then
        assertThat(result.size()).isEqualTo(tasks.size());
        result.forEach(task -> {
            assertThat(task.getManagerName()).isEqualTo(user.getName());
            assertThat(task.getManagerAvatar()).isEqualTo(user.getAvatar());
        });
        verify(taskRepository, times(1)).findAllFilterByManager(any(Project.class), any(List.class), eq(false));
    }

    @Test
    void 프로젝트의_담당자가_없는_테스크들을_조회한다() {
        // given
        Long projectId = 3242L;
        Project project = ProjectProvider.createProject(25332L);
        List<Task> tasks = List.of(
                TaskProvider.createTask(null, project, null),
                TaskProvider.createTask(null, project, null)
        );
        List<Long> managers = new ArrayList<>();
        managers.add(null);

        given(projectService.initializeProjectInfo(projectId))
                .willReturn(project);
        given(taskRepository.findAllFilterByManager(any(Project.class), anyList(), eq(true)))
                .willReturn(tasks);

        // when
        List<ProjectTaskSimpleResponse> result = taskService.getTasksFilterByManager(projectId, managers, true);

        // then
        assertThat(result.size()).isEqualTo(tasks.size());
        result.forEach(task -> {
            assertThat(task.getManagerName()).isNull();
            assertThat(task.getManagerAvatar()).isNull();
        });
        verify(userService, never()).findUserById(anyLong());
        verify(taskRepository, times(1)).findAllFilterByManager(any(Project.class), anyList(), eq(true));
    }

    @Test
    void 검색한_키워드를_포함하는_제목을_가진_태스크를_조회한다() {
        // given
        Long projectId = 1L;
        String keyword = "api";
        Project project = ProjectProvider.createProject(5L);
        List<Task> taskList = List.of(
                TaskProvider.createTaskWithTitle(null, project, null, "implement backlog task search api"),
                TaskProvider.createTaskWithTitle(null, project, null, "refactor backlog filter api")
        );

        given(projectService.initializeProjectInfo(eq(projectId)))
                .willReturn(project);
        given(taskRepository.findTasksSearchByKeyword(any(Project.class), eq(keyword)))
                .willReturn(taskList);

        // when
        List<ProjectTaskSimpleResponse> result = taskService.searchTasksByKeyword(projectId, keyword);

        // then
        assertThat(result).hasSize(2);
        result.forEach(task -> assertThat(task.getTitle().contains(keyword)));
        verify(taskRepository, times(1)).findTasksSearchByKeyword(any(Project.class), anyString());
    }

}
