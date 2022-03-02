package kr.kro.colla.task.task.presentation;

import kr.kro.colla.task.task.presentation.dto.*;
import kr.kro.colla.task.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/projects")
@Controller
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/tasks")
    public ResponseEntity<Void> createTask(@Valid CreateTaskRequest createTaskRequest) {
        Long taskId = taskService.createTask(createTaskRequest);
        URI redirectUrl = URI.create("/api/projects/tasks/" + taskId);

        return ResponseEntity.created(redirectUrl)
                .build();
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<ProjectTaskResponse> getTask(@PathVariable Long taskId) {
        ProjectTaskResponse task = taskService.getTask(taskId);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable Long taskId, @Valid UpdateTaskRequest updateTaskRequest) {
        taskService.updateTask(taskId, updateTaskRequest);

        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable Long taskId, @Valid @RequestBody UpdateTaskStatusRequest request) {
        taskService.updateTaskStatus(taskId, request.getStatusName());

        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/{projectId}/tasks/created-date")
    public ResponseEntity<List<ProjectTaskSimpleResponse>> getTasksOrderByCreatedDate(@PathVariable Long projectId, @RequestParam(defaultValue = "false") Boolean ascending) {
        List<ProjectTaskSimpleResponse> taskList = taskService.getTasksOrderByCreatedDate(projectId, ascending);

        return ResponseEntity.ok(taskList);
    }
  
    @GetMapping("/{projectId}/tasks/priority")
    public ResponseEntity<List<ProjectTaskSimpleResponse>> getTasksOrderByPriority(@PathVariable Long projectId, @RequestParam(defaultValue = "false") Boolean ascending) {
        List<ProjectTaskSimpleResponse> taskList = taskService.getTasksOrderByPriority(projectId, ascending);

        return ResponseEntity.ok(taskList);
    }

    @GetMapping("/{projectId}/tasks/statuses")
    public ResponseEntity<List<ProjectTaskSimpleResponse>> getTasksFilterByStatuses(@PathVariable Long projectId, @RequestParam String status) {
        List<ProjectTaskSimpleResponse> taskList = taskService.getTasksFilterByStatus(projectId, status);

        return ResponseEntity.ok(taskList);
    }

    @GetMapping("/{projectId}/tasks/managers")
    public ResponseEntity<List<ProjectTaskSimpleResponse>> getTasksFilterByManagers(@PathVariable Long projectId, @RequestParam(required = false) Long managerId) {
        List<ProjectTaskSimpleResponse> taskList = taskService.getTasksFilterByManager(projectId, managerId);

        return ResponseEntity.ok(taskList);
    }

    @GetMapping("/{projectId}/tasks/tags")
    public ResponseEntity<List<ProjectTaskSimpleResponse>> getTasksFilterByTags(@PathVariable Long projectId, @RequestParam List<String> tags) {
        List<ProjectTaskSimpleResponse> taskList = taskService.getTasksFilterByTags(projectId, tags);

        return ResponseEntity.ok(taskList);
    }

    @GetMapping("/{projectId}/tasks/story")
    public ResponseEntity<List<ProjectStoryTaskResponse>> getTasksGroupByStory(@PathVariable Long projectId) {
        List<ProjectStoryTaskResponse> taskList = taskService.getTasksGroupByStory(projectId);

        return ResponseEntity.ok(taskList);
    }

}
