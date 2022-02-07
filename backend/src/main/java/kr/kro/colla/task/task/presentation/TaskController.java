package kr.kro.colla.task.task.presentation;

import kr.kro.colla.task.task.presentation.dto.CreateTaskRequest;
import kr.kro.colla.task.task.presentation.dto.ProjectTaskResponse;
import kr.kro.colla.task.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

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

}
