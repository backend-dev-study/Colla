package kr.kro.colla.story.presentation;

import kr.kro.colla.common.ControllerTest;
import kr.kro.colla.common.fixture.ProjectProvider;
import kr.kro.colla.common.fixture.StoryProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.story.presentation.dto.ProjectStoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
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

}
