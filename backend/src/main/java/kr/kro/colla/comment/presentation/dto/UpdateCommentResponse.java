package kr.kro.colla.comment.presentation.dto;

import kr.kro.colla.comment.domain.Comment;
import lombok.Getter;

@Getter
public class UpdateCommentResponse {

    private String contents;

    public UpdateCommentResponse(Comment comment) {
        this.contents = comment.getContents();
    }

}
