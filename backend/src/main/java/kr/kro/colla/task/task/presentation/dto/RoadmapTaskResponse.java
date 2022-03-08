package kr.kro.colla.task.task.presentation.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RoadmapTaskResponse {

    private String title;

    private String manager;

    private List<String> tags;

}
