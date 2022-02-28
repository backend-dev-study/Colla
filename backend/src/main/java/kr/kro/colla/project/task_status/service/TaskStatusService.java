package kr.kro.colla.project.task_status.service;

import kr.kro.colla.exception.exception.project.task_status.TaskStatusNotFoundException;
import kr.kro.colla.project.task_status.domain.TaskStatus;
import kr.kro.colla.project.task_status.domain.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    public TaskStatus findTaskStatusByName(String name) {
        return taskStatusRepository.findByName(name)
                .orElseThrow(TaskStatusNotFoundException::new);
    }

    public TaskStatus findTaskStatusById(Long statusId) {
        return taskStatusRepository.findById(statusId)
                .orElseThrow(TaskStatusNotFoundException::new);
    }

}
