package kr.kro.colla.project.project.presentation.dto;

import kr.kro.colla.project.project.service.dto.ProjectTaskResponse;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
public class ProjectResponse {

    private Long id;

    private Long managerId;

    private String name;

    private String description;

    private String thumbnail;

    private List<UserProfileResponse> members;

    private Map<String, List<ProjectTaskResponse>> tasks;

}
