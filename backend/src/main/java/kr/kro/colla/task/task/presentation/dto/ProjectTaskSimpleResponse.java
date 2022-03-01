package kr.kro.colla.task.task.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectTaskSimpleResponse {

    private Long id;

    private String managerName;

    private String managerAvatar;

    private String title;

    private Integer priority;

    private String status;

    private List<String> tags;

}
