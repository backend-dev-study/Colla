package kr.kro.colla.comment.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.presentation.argument_resolver.Authenticated;
import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.comment.presentation.dto.*;
import kr.kro.colla.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CreateCommentResponse> saveComment(@Authenticated LoginUser loginUser, @PathVariable Long taskId,
                                                             @Valid @RequestBody CreateCommentRequest createCommentRequest) {
        CreateCommentResponse createCommentResponse = commentService.saveComment(loginUser.getId(), taskId, createCommentRequest);

        return ResponseEntity.ok(createCommentResponse);
    }

    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<List<TaskCommentResponse>> getAllComments(@PathVariable Long taskId) {
        List<TaskCommentResponse> allComments = commentService.getAllComments(taskId);

        return ResponseEntity.ok(allComments);
    }

    @PutMapping("/tasks/comments/{commentId}")
    public ResponseEntity<UpdateCommentResponse> updateComment(@PathVariable Long commentId, @Valid @RequestBody UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentService.updateComment(commentId, updateCommentRequest);

        return ResponseEntity.ok(new UpdateCommentResponse(comment));
    }

    @DeleteMapping("/tasks/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok()
                .build();
    }

}
