package kr.kro.colla.user.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjectService projectService;

    private Long managerId = 3L;
    private String name = "프로젝트 이름", desc = "프로젝트 설명";

    @Test
    void 사용자_프로젝트_생성_후_반환한다() throws Exception {
        // given
        CreateProjectRequest createProjectRequest = CreateProjectRequest.builder()
                .name(name)
                .description(desc)
                .build();
        Project project = Project.builder()
                .managerId(managerId)
                .name(name)
                .description(desc)
                .build();
        given(projectService.createProject(eq(managerId), any(CreateProjectRequest.class))).willReturn(project);

        String content = new ObjectMapper().writeValueAsString(createProjectRequest);

        // when
        ResultActions perform = mockMvc.perform(post("/users/"+managerId+"/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.managerId").value(managerId))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(desc));
    }

    @Test
    void 사용자_프로젝트_생성_실패_시_에러를_반환한다() throws Exception {
        // given
        CreateProjectRequest createProjectRequest = CreateProjectRequest.builder()
                .description(desc)
                .build();
        String content = new ObjectMapper().writeValueAsString(createProjectRequest);

        // when
        ResultActions perform = mockMvc.perform(post("/users/"+managerId+"/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("name : must not be null"));
    }
}