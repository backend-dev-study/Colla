package kr.kro.colla.project.project.presentation;

import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.project.project.presentation.dto.ProjectResponse;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.project.project.service.dto.ProjectTaskResponse;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.UserProfileResponse;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.service.UserProjectService;
import kr.kro.colla.utils.CookieManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private CookieManager cookieManager;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserProjectService userProjectService;

    private String accessToken = "token";
    private LoginUser loginUser;

    @BeforeEach
    void setUp() {
        String accessToken = "token";
        loginUser = new LoginUser(345234L);

        given(cookieManager.parseCookies(any(Cookie[].class), eq("accessToken")))
                .willReturn(new Cookie("accessToken", accessToken));
        given(authService.validateAccessToken(eq(accessToken)))
                .willReturn(true);
        given(authService.findUserFromToken(accessToken))
                .willReturn(loginUser);
    }

    @Test
    void projectId에_해당하는_프로젝트를_조회한다() throws Exception {
        // given
        Long projectId = 1L;
        Map<String, List<ProjectTaskResponse>> tasks = new HashMap<>();
        tasks.put("To Do", new ArrayList<>());
        tasks.put("In Progress", new ArrayList<>());
        tasks.put("Done", new ArrayList<>());
        User user = User.builder()
                .name("kykapple")
                .githubId("kykapple")
                .avatar("github_content")
                .build();
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(projectId)
                .managerId(loginUser.getId())
                .name("project name")
                .description("project description")
                .thumbnail("s3_content")
                .members(List.of(new UserProfileResponse(user)))
                .tasks(tasks)
                .build();

        given(projectService.getProject(eq(projectId)))
                .willReturn(projectResponse);

        // when
        ResultActions perform = mockMvc.perform(get("/projects/" + projectId)
                .cookie(new Cookie("accessToken", this.accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(projectResponse.getName()))
                .andExpect(jsonPath("$.description").value(projectResponse.getDescription()))
                .andExpect(jsonPath("$.members").isArray())
                .andExpect(jsonPath("$.tasks").isMap())
                .andExpect(jsonPath("$.tasks.Done").isEmpty());
    }

}