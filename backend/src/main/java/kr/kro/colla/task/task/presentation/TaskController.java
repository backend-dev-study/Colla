package kr.kro.colla.task.task.presentation;

import kr.kro.colla.task.task.presentation.dto.CreateTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/tasks")
@Controller
public class TaskController {

    @PostMapping("/")
    public ResponseEntity createTask(@ModelAttribute CreateTaskRequest createTaskRequest) {
        return null;
    }

}
