package kr.kro.colla.comment.presentation.dto;

import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class TaskCommentResponse {

    private Long id;

    private UserProfileResponse writer;

    private String contents;

    private List<TaskCommentResponse> subComments;

    public TaskCommentResponse(Comment comment) {
        this.id = comment.getId();
        this.writer = new UserProfileResponse(comment.getUser());
        this.contents = comment.getContents();
        this.subComments = new ArrayList<>();
    }

    public void addSubComment(Comment comment) {
        this.subComments.add(new TaskCommentResponse(comment));
    }

}
