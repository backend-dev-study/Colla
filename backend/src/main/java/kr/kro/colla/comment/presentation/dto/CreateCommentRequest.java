package kr.kro.colla.comment.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateCommentRequest {

    @NotNull
    private Long taskId;

    private Long superCommentId;

    @NotBlank
    private String contents;

}
