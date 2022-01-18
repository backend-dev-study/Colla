package kr.kro.colla.project.project.presentation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class ProjectMemberDecision {
    @NotNull
    private boolean accept;
}
