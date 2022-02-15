package kr.kro.colla.comment.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateCommentRequest {

    private Long superCommentId;

    @NotBlank
    private String contents;

}
