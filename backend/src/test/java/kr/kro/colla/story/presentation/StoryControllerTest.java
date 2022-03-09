package kr.kro.colla.story.presentation;

import kr.kro.colla.common.ControllerTest;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.StoryProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.story.presentation.dto.ProjectStoryResponse;
import kr.kro.colla.story.presentation.dto.UpdateStoryPeriodRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.contains;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoryController.class)
class StoryControllerTest extends ControllerTest {

    @Test
    void 프로젝트의_모든_스토리를_조회한다() throws Exception {
        // given
        Long projectId = 5L;
        Project project = ProjectProvider.createProject(1L);
        List<ProjectStoryResponse> storyList = List.of(
                new ProjectStoryResponse(StoryProvider.createStory(project, "first story")),
                new ProjectStoryResponse(StoryProvider.createStory(project, "second story"))
        );

        given(storyService.getProjectStories(eq(projectId)))
                .willReturn(storyList);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId + "/all-stories")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(storyList.size()))
                .andExpect(jsonPath("$[*].title", contains(storyList.get(0).getTitle(), storyList.get(1).getTitle())));
        verify(storyService, times(1)).getProjectStories(anyLong());
    }

    @Test
    void 스토리의_진행_기간을_설정한다() throws Exception {
        // given
        willDoNothing()
                .given(storyService)
                .updateStoryPeriod(anyLong(), any(UpdateStoryPeriodRequest.class));

        // when
        ResultActions perform = mockMvc.perform(patch("/projects/stories/" + 1L + "/period")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("startAt", "2022-03-09")
                .param("endAt", "2022-03-12"));

        // then
        perform.andExpect(status().isOk());
        verify(storyService, times(1)).updateStoryPeriod(anyLong(), any(UpdateStoryPeriodRequest.class));
    }

    @Test
    void 스토리의_진행_기간이_비어있다면_설정에_실패한다() throws Exception {
        // when
        ResultActions perform = mockMvc.perform(patch("/projects/stories/" + 1L + "/period")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(anyOf(is("startAt : must not be null"), is("endAt : must not be null"))));
    }

}
