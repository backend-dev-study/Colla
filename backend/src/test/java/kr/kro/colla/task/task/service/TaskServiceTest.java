package kr.kro.colla.task.task.service;

import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.StoryProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.service.TaskStatusService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.service.StoryService;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import kr.kro.colla.task.task.presentation.dto.CreateTaskRequest;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskResponse;
import kr.kro.colla.task.task.presentation.dto.UpdateTaskRequest;
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

import java.util.List;
import java.util.Optional;

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
        User user = UserProvider.createUser();
        ReflectionTestUtils.setField(user, "id", managerId);

        Project project = ProjectProvider.createProject(user.getId());
        Story story = StoryProvider.createStory(project, "user can login with github");
        Task task = TaskProvider.createTask(user.getId(), project, story);
        ReflectionTestUtils.setField(task, "id", taskId);

        given(taskRepository.findById(eq(taskId)))
                .willReturn(Optional.of(task));
        given(userService.findUserById(eq(managerId)))
                .willReturn(user);

        // when
        ProjectTaskResponse projectTaskResponse = taskService.getTask(taskId);

        // then
        assertThat(projectTaskResponse.getId()).isEqualTo(taskId);
        assertThat(projectTaskResponse.getTitle()).isEqualTo(task.getTitle());
        assertThat(projectTaskResponse.getStory()).isEqualTo(story.getTitle());
        assertThat(projectTaskResponse.getManager()).isEqualTo(user.getName());
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
}
