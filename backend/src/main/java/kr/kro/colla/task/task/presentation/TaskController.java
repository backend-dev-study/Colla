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
@RequestMapping("/projects/tasks")
@Controller
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Void> createTask(@Valid CreateTaskRequest createTaskRequest) {
        Long taskId = taskService.createTask(createTaskRequest);
        URI redirectUrl = URI.create("/api/projects/tasks/" + taskId);

        return ResponseEntity.created(redirectUrl)
                .build();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ProjectTaskResponse> getTask(@PathVariable Long taskId) {
        ProjectTaskResponse projectTaskResponse = taskService.getTask(taskId);

        return ResponseEntity.ok(projectTaskResponse);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable Long taskId, @Valid UpdateTaskRequest updateTaskRequest) {
        taskService.updateTask(taskId, updateTaskRequest);

        return ResponseEntity.ok()
                .build();
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<Void> updateTaskStatus(@PathVariable Long taskId, @Valid @RequestBody UpdateTaskStatusRequest request) {
        taskService.updateTaskStatus(taskId, request.getStatusName());

        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("/priority")
    public ResponseEntity<List<ProjectTaskResponse>> getTasksOrderByPriority(@Valid @RequestBody ProjectTaskRequest projectTaskRequest) {
        List<ProjectTaskResponse> taskList = taskService.getTasksOrderByPriority(projectTaskRequest.getProjectId());

        return ResponseEntity.ok(taskList);
    }

}
