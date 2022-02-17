package kr.kro.colla.comment.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.presentation.argument_resolver.Authenticated;
import kr.kro.colla.comment.presentation.dto.CreateCommentRequest;
import kr.kro.colla.comment.presentation.dto.CreateCommentResponse;
import kr.kro.colla.comment.presentation.dto.TaskCommentResponse;
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

}
