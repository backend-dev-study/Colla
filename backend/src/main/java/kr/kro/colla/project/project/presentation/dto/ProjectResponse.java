package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.project.project.service.dto.ProjectTaskResponse;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProjectResponse {

    private Long id;

    private Long managerId;

    private String name;

    private String description;

    private String thumbnail;

    private List<UserProfileResponse> members;

    private List<ProjectTaskResponse> tasks;

}
