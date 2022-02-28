package kr.kro.colla.task.task.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class ProjectStoryTaskResponse {

    private String story;

    private List<ProjectTaskSimpleResponse> taskList;

    public ProjectStoryTaskResponse(String story, List<ProjectTaskSimpleResponse> taskList) {
        this.story = story;
        this.taskList = taskList;
    }

}
