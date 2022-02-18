package kr.kro.colla.comment.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateCommentRequest {

    @NotBlank
    private String contents;

}
