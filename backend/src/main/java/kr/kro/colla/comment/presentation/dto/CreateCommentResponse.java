package kr.kro.colla.comment.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateCommentResponse {

    private Long id;

    private Long userId;

    private Long superCommentId;

    private String contents;

}
