package kr.kro.colla.task.task_tag.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.kro.colla.exception.exception.task.tag.InvalidTagFormatException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.tag.domain.Tag;
import kr.kro.colla.task.tag.service.TagService;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task_tag.domain.TaskTag;
import kr.kro.colla.task.task_tag.domain.repository.TaskTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class TaskTagService {

    private final TagService tagService;
    private final TaskTagRepository taskTagRepository;
    private final ObjectMapper objectMapper;

    public void addNewTag(Project project, Tag tag) {
        TaskTag taskTag = new TaskTag(project, tag);

        taskTagRepository.save(taskTag);
    }

    public void setTaskTag(Task task, String tags) {
        try {
            List<String> tagNames = objectMapper.readValue(tags, new TypeReference<List<String>>() {});

            tagService.findTagsByName(tagNames)
                    .stream()
                    .map(tag -> new TaskTag(task, tag))
                    .forEach(taskTagRepository::save);
        } catch (JsonProcessingException e) {
            throw new InvalidTagFormatException();
        }
    }

    public List<TaskTag> translateTaskTags(Task task, String tags) {
        try {
            List<String> tagNames = objectMapper.readValue(tags, new TypeReference<List<String>>() {});

            return tagService.findTagsByName(tagNames)
                    .stream()
                    .map(tag -> new TaskTag(task, tag))
                    .collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new InvalidTagFormatException();
        }
    }

}
