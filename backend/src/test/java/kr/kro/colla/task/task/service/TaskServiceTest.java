package kr.kro.colla.task.task.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.service.TaskStatusService;
import kr.kro.colla.story.domain.Story;
import kr.kro.colla.story.service.StoryService;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.domain.repository.TaskRepository;
import kr.kro.colla.task.task.presentation.dto.CreateTaskRequest;
import kr.kro.colla.task.task_tag.service.TaskTagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

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
        CreateTaskRequest createTaskRequest = new CreateTaskRequest("task title", null, "task description", 4, taskStatusName, "[\"backend\"]", 1L, storyTitle, "[]");
        Project project = Project.builder()
                .name("collaboration")
                .description("collaboration tool")
                .build();
        Story story = Story.builder()
                .title(storyTitle)
                .preStories("[]")
                .project(project)
                .build();
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
        given(taskRepository.save(any(Task.class)))
                .willReturn(task);

        // when
        Long taskId = taskService.createTask(createTaskRequest);

        // then
        assertThat(taskId).isNotNull();
        verify(projectService, times(1)).findProjectById(1L);
        verify(storyService, times(1)).findStoryByTitle(storyTitle);
        verify(taskStatusService, times(1)).findTaskStatusByName(taskStatusName);
        verify(taskTagService, times(1)).setTaskTag(any(Task.class), anyString());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

}
