package kr.kro.colla.comment.service;

import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.comment.domain.repository.CommentRepository;
import kr.kro.colla.comment.presentation.dto.CreateCommentRequest;
import kr.kro.colla.comment.presentation.dto.CreateCommentResponse;
import kr.kro.colla.comment.presentation.dto.TaskCommentResponse;
import kr.kro.colla.comment.presentation.dto.UpdateCommentRequest;
import kr.kro.colla.exception.exception.comment.CommentNotFoundException;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.service.TaskService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import kr.kro.colla.user.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {

    private final UserService userService;
    private final TaskService taskService;
    private final CommentRepository commentRepository;

    public CreateCommentResponse saveComment(Long userId, Long taskId, CreateCommentRequest createCommentRequest) {
        User user = userService.findUserById(userId);
        Task task = taskService.findTaskById(taskId);
        Comment superComment = createCommentRequest.getSuperCommentId() != null
                ? findCommentById(createCommentRequest.getSuperCommentId())
                : null;
        Long superCommentId = superComment == null ? null : superComment.getId();

        Comment comment = Comment.builder()
                .user(user)
                .task(task)
                .superComment(superComment)
                .contents(createCommentRequest.getContents())
                .build();
        commentRepository.save(comment);

        return CreateCommentResponse.builder()
                .id(comment.getId())
                .writer(new UserProfileResponse(user))
                .superCommentId(superCommentId)
                .contents(comment.getContents())
                .build();
    }

    public List<TaskCommentResponse> getAllComments(Long taskId) {
        Task task = taskService.findTaskById(taskId);
        List<Comment> allComments = commentRepository.findAll(task);
        Map<Long, TaskCommentResponse> comments = new HashMap<>();

        for (Comment comment : allComments) {
            if (comment.getSuperComment() == null) {
                comments.put(comment.getId(), new TaskCommentResponse(comment));
                continue;
            }

            comments.get(comment.getSuperComment().getId())
                    .addSubComment(comment);
        }

        return new ArrayList<>(comments.values());
    }

    public Comment updateComment(Long commentId, UpdateCommentRequest updateCommentRequest) {
        Comment comment = findCommentById(commentId);
        comment.updateContents(updateCommentRequest.getContents());

        return comment;
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

}
