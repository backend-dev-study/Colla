package kr.kro.colla.task.task.presentation.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProjectTaskResponse {

    private Long id;

    private String manager;

    private String title;

    private String description;

    private String story;

    private String preTasks;

    private Integer priority;

    private String status;

    private List<String> tags;

}
