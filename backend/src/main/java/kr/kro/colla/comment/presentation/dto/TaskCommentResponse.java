package kr.kro.colla.comment.presentation.dto;

import kr.kro.colla.comment.domain.Comment;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TaskCommentResponse {

    private Long id;

    private Long userId;

    private String contents;

    private List<TaskCommentResponse> subComments;

    public TaskCommentResponse(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUser().getId();
        this.contents = comment.getContents();
        this.subComments = new ArrayList<>();
    }

    public void addSubComment(Comment comment) {
        this.subComments.add(new TaskCommentResponse(comment));
    }

}
