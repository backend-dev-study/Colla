package kr.kro.colla.task.task_tag.service;

import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import kr.kro.colla.task.task_tag.domain.repository.TaskTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskTagService {

    private final TaskTagRepository taskTagRepository;

    public void addNewTag(Project project, Tag tag) {
        TaskTag taskTag = TaskTag.builder()
                .project(project)
                .tag(tag)
                .build();

        taskTagRepository.save(taskTag);
    }

}
