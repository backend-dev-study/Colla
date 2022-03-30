package kr.kro.colla.task.task.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ManagerTaskCountResponse {

    private String managerName;

    private List<TaskCountResponse> taskCounts;
}
