package kr.kro.colla.comment.presentation;

import com.fasterxml.jackson.databind.type.CollectionType;
import kr.kro.colla.comment.domain.Comment;
import kr.kro.colla.comment.presentation.dto.CreateCommentRequest;
import kr.kro.colla.comment.presentation.dto.CreateCommentResponse;
import kr.kro.colla.comment.presentation.dto.TaskCommentResponse;
import kr.kro.colla.comment.presentation.dto.UpdateCommentRequest;
import kr.kro.colla.common.ControllerTest;
import kr.kro.colla.common.fixture.CommentProvider;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.TaskProvider;
import kr.kro.colla.common.fixture.UserProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.task.task.domain.Task;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends ControllerTest {

    @Test
    void 댓글_등록에_성공한다() throws Exception {
        // given
        User user = UserProvider.createUser();
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(null, "comment contents");
        CreateCommentResponse createCommentResponse = CreateCommentResponse.builder()
                .writer(new UserProfileResponse(user))
                .contents(createCommentRequest.getContents())
                .build();
        String content = objectMapper.writeValueAsString(createCommentRequest);

        given(commentService.saveComment(anyLong(), anyLong(), any(CreateCommentRequest.class)))
                .willReturn(createCommentResponse);

        // when
        ResultActions perform = mockMvc.perform(post("/tasks/" + 1L + "/comments")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.writer.displayName").value(user.getName()))
                .andExpect(jsonPath("$.writer.avatar").value(user.getAvatar()))
                .andExpect(jsonPath("$.contents").value(createCommentRequest.getContents()));
        verify(commentService, times(1)).saveComment(anyLong(), anyLong(), any(CreateCommentRequest.class));
    }

    @Test
    void 댓글의_내용이_없을_경우_댓글을_등록할_수_없다() throws Exception {
        // given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(null, "");
        String content = objectMapper.writeValueAsString(createCommentRequest);

        // when
        ResultActions perform = mockMvc.perform(post("/tasks/" + 1L + "/comments")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("contents : must not be blank"));
        verify(commentService, never()).saveComment(anyLong(), anyLong(), any(CreateCommentRequest.class));
    }

    @Test
    void 대댓글을_포함한_모든_댓글을_조회한다() throws Exception {
        // given
        Map<Long, TaskCommentResponse> allComments = new HashMap<>();

        User user = UserProvider.createUser();
        Project project = ProjectProvider.createProject(user.getId());
        Task task = TaskProvider.createTask(user.getId(), project, null);

        Comment comment1 = CommentProvider.createComment(user, task, null, "first comment contents");
        Comment comment2 = CommentProvider.createComment(user, task, comment1, "first comment's subComment");
        Comment comment3 = CommentProvider.createComment(user, task, null, "another comment contents");

        allComments.put(1L, new TaskCommentResponse(comment1));
        allComments.get(1L).addSubComment(comment2);
        allComments.put(3L, new TaskCommentResponse(comment3));

        given(commentService.getAllComments(anyLong()))
                .willReturn(new ArrayList<>(allComments.values()));

        // when
        ResultActions perform = mockMvc.perform(get("/tasks/" + 1L + "/comments")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult result = perform.andReturn();
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, TaskCommentResponse.class);
        List<TaskCommentResponse> allCommentsResponse = objectMapper.readValue(result.getResponse().getContentAsString(), collectionType);

        perform
                .andExpect(status().isOk());

        List<TaskCommentResponse> subComments = allCommentsResponse.get(0).getSubComments();
        assertThat(allCommentsResponse.size()).isEqualTo(2);
        assertThat(allCommentsResponse.get(0).getContents()).isEqualTo(comment1.getContents());
        assertThat(subComments.size()).isEqualTo(1);
        assertThat(subComments.get(0).getContents()).isEqualTo(comment2.getContents());
        assertThat(allCommentsResponse.get(1).getContents()).isEqualTo(comment3.getContents());
        verify(commentService, times(1)).getAllComments(anyLong());
    }

    @Test
    void 작성한_댓글_내용을_수정한다() throws Exception {
        // given
        Long commentId = 1L;
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("new contents");
        String content = objectMapper.writeValueAsString(updateCommentRequest);
        Comment comment = CommentProvider.createComment(null, null, null, "new contents");

        given(commentService.updateComment(eq(commentId), any(UpdateCommentRequest.class)))
                .willReturn(comment);

        // when
        ResultActions perform = mockMvc.perform(put("/tasks/comments/" + commentId)
                .cookie(new Cookie("accessToken", this.accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contents").value(updateCommentRequest.getContents()));
        verify(commentService, times(1)).updateComment(eq(commentId), any(UpdateCommentRequest.class));
    }

    @Test
    void 빈_내용으로는_댓글을_수정할_수_없다() throws Exception {
        // given
        Long commentId = 1L;
        UpdateCommentRequest updateCommentRequest = new UpdateCommentRequest("");
        String content = objectMapper.writeValueAsString(updateCommentRequest);

        // when
        ResultActions perform = mockMvc.perform(put("/tasks/comments/" + commentId)
                .cookie(new Cookie("accessToken", this.accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("contents : must not be blank"));
    }

}
