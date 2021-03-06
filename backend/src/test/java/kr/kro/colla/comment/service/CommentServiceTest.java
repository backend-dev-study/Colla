package kr.kro.colla.comment.service;

import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.comment.domain.repository.CommentRepository;
import kr.kro.colla.comment.presentation.dto.CreateCommentRequest;
import kr.kro.colla.comment.presentation.dto.CreateCommentResponse;
import kr.kro.colla.comment.presentation.dto.TaskCommentResponse;
import kr.kro.colla.comment.presentation.dto.UpdateCommentRequest;
import kr.kro.colla.common.fixture.CommentProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.task.task.service.TaskService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TaskService taskService;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void 댓글_등록에_성공한다() {
        // given
        Long userId = 1L, taskId = 1L;
        User user = UserProvider.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        Project project = ProjectProvider.createProject(user.getId());
        Task task = TaskProvider.createTask(user.getId(), project, null);
        ReflectionTestUtils.setField(task, "id", taskId);

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(null, "comment contents");

        given(userService.findUserById(eq(userId)))
                .willReturn(user);
        given(taskService.findTaskById(taskId))
                .willReturn(task);

        // when
        CreateCommentResponse createCommentResponse = commentService.saveComment(userId, taskId, createCommentRequest);

        // then
        assertThat(createCommentResponse.getWriter().getDisplayName()).isEqualTo(user.getName());
        assertThat(createCommentResponse.getContents()).isEqualTo(createCommentRequest.getContents());
        assertThat(createCommentResponse.getSuperCommentId()).isNull();
        verify(userService, times(1)).findUserById(eq(userId));
        verify(taskService, times(1)).findTaskById(eq(taskId));
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void 대댓글_등록에_성공한다() {
        // given
        Long userId = 1L, taskId = 5L, superCommentId = 3L;
        User user = UserProvider.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        Project project = ProjectProvider.createProject(user.getId());
        Task task = TaskProvider.createTask(user.getId(), project, null);
        ReflectionTestUtils.setField(task, "id", taskId);

        Comment superComment = CommentProvider.createComment(user, task, null, "first comment contents");
        ReflectionTestUtils.setField(superComment, "id", superCommentId);
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(superCommentId, "comment contents");

        given(userService.findUserById(eq(userId)))
                .willReturn(user);
        given(taskService.findTaskById(taskId))
                .willReturn(task);
        given(commentRepository.findById(eq(superCommentId)))
                .willReturn(Optional.of(superComment));

        // when
        CreateCommentResponse createCommentResponse = commentService.saveComment(userId, taskId, createCommentRequest);

        // then
        assertThat(createCommentResponse.getSuperCommentId()).isEqualTo(superCommentId);
        verify(commentRepository, times(1)).findById(eq(superCommentId));
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void 대댓글을_포함한_모든_댓글을_조회한다() {
        // given
        Long userId = 1L, taskId = 5L;
        User user = UserProvider.createUser();
        ReflectionTestUtils.setField(user, "id", userId);

        Project project = ProjectProvider.createProject(user.getId());
        Task task = TaskProvider.createTask(user.getId(), project, null);
        ReflectionTestUtils.setField(task, "id", taskId);

        Comment comment1 = CommentProvider.createComment(user, task, null, "first comment contents");
        ReflectionTestUtils.setField(comment1, "id", 1L);
        Comment comment2 = CommentProvider.createComment(user, task, comment1, "first comment's subComment");
        ReflectionTestUtils.setField(comment2, "id", 2L);
        Comment comment3 = CommentProvider.createComment(user, task, null, "another comment contents");
        ReflectionTestUtils.setField(comment3, "id", 3L);

        List<Comment> allCommentList = List.of(comment1, comment2, comment3);

        given(taskService.findTaskById(eq(taskId)))
                .willReturn(task);
        given(commentRepository.findAll(any(Task.class)))
                .willReturn(allCommentList);

        // when
        List<TaskCommentResponse> allComments = commentService.getAllComments(taskId);

        // then
        assertThat(allComments.size()).isEqualTo(2);

        List<TaskCommentResponse> subComments1 = allComments.get(0).getSubComments();
        assertThat(allComments.get(0).getContents()).isEqualTo(comment1.getContents());
        assertThat(subComments1.size()).isEqualTo(1);
        assertThat(subComments1.get(0).getContents()).isEqualTo(comment2.getContents());

        List<TaskCommentResponse> subComments2 = allComments.get(1).getSubComments();
        assertThat(allComments.get(1).getContents()).isEqualTo(comment3.getContents());
        assertThat(subComments2.isEmpty());
    }

    @Test
    void 작성한_댓글_내용을_수정한다() {
        // given
        Long commentId = 1L;
        Comment comment = CommentProvider.createComment(null, null, null, "old contents");
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("new contents");

        given(commentRepository.findById(eq(commentId)))
                .willReturn(Optional.of(comment));

        // when
        Comment result = commentService.updateComment(commentId, updateCommentRequest);

        // then
        assertThat(result.getContents()).isEqualTo(updateCommentRequest.getContents());
    }

}
