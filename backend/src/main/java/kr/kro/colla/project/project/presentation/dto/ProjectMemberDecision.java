package kr.kro.colla.project.project.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberDecision {
    @NotNull
    private boolean accept;

    @NotNull
    private Long noticeId;
}
