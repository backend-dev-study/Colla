package kr.kro.colla.comment.presentation.dto;

import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
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

    private UserProfileResponse writer;

    private Long superCommentId;

    private String contents;

}
