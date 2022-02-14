package kr.kro.colla.comment.presentation;

import kr.kro.colla.comment.presentation.dto.CreateCommentRequest;
import kr.kro.colla.comment.presentation.dto.CreateCommentResponse;
import kr.kro.colla.common.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends ControllerTest {

    @Test
    void 댓글_등록에_성공한다() throws Exception {
        // given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(loginUser.getId(), null, "comment contents");
        CreateCommentResponse createCommentResponse = CreateCommentResponse.builder()
                .userId(loginUser.getId())
                .contents(createCommentRequest.getContents())
                .build();
        String content = objectMapper.writeValueAsString(createCommentRequest);

        given(commentService.saveComment(anyLong(), any(CreateCommentRequest.class)))
                .willReturn(createCommentResponse);

        // when
        ResultActions perform = mockMvc.perform(post("/task/comments")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(loginUser.getId()))
                .andExpect(jsonPath("$.contents").value(createCommentRequest.getContents()));
    }

    @Test
    void 댓글의_내용이_없을_경우_댓글을_등록할_수_없다() throws Exception {
        // given
        CreateCommentRequest createCommentRequest = new CreateCommentRequest(loginUser.getId(), null, "");
        String content = objectMapper.writeValueAsString(createCommentRequest);

        // when
        ResultActions perform = mockMvc.perform(post("/task/comments")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("contents : must not be blank"));
    }

}
