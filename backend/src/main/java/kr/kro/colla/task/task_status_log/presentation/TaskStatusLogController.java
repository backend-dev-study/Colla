package kr.kro.colla.task.task_status_log.presentation;

import kr.kro.colla.task.task_status_log.presentation.dto.TaskStatusLogResponse;
import kr.kro.colla.task.task_status_log.service.TaskStatusLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/projects")
@RestController
public class TaskStatusLogController {

    private final TaskStatusLogService taskStatusLogService;

    @GetMapping("/{projectId}/task-status-log")
    public ResponseEntity<Map<String, List<TaskStatusLogResponse>>> getWeeklyTaskStatusLog(@PathVariable Long projectId) {
        Map<String, List<TaskStatusLogResponse>> weeklyTaskStatusLog = taskStatusLogService.getWeeklyTaskStatusLog(projectId);

        return ResponseEntity.ok(weeklyTaskStatusLog);
    }

}
