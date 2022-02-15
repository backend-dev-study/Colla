package kr.kro.colla.task.task.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
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
