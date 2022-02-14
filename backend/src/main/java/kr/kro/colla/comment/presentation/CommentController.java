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
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/task/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CreateCommentResponse> saveComment(@Authenticated LoginUser loginUser, @Valid @RequestBody CreateCommentRequest createCommentRequest) {
        CreateCommentResponse createCommentResponse = commentService.saveComment(loginUser.getId(), createCommentRequest);

        return ResponseEntity.ok(createCommentResponse);
    }

    @GetMapping
    public ResponseEntity<Map<Long, TaskCommentResponse>> getAllComments(@RequestBody Long taskId) {
        Map<Long, TaskCommentResponse> allComments = commentService.getAllComments(taskId);

        return ResponseEntity.ok(allComments);
    }

}
