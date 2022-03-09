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
public class RoadmapTaskResponse {

    private String title;

    private String manager;

    private List<String> tags;

}
