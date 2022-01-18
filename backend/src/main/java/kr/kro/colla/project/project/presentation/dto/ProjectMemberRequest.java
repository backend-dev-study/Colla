package kr.kro.colla.project.project.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberRequest {
    @NotEmpty
    private String githubId;
}
