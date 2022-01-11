package kr.kro.colla.user.user.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.kro.colla.auth.presentation.interceptor.AuthInterceptor;
import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.exception.exception.user.UserNotFoundException;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.service.UserProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.net.UnknownServiceException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthInterceptor authInterceptor;
    @MockBean
    private AuthService authService;

    @MockBean
    private ProjectService projectService;
    @MockBean
    private UserProjectService userProjectService;
    @MockBean
    private UserService userService;

    private Long managerId = 3L;
    private String name = "프로젝트 이름", desc = "프로젝트 설명";

    @BeforeEach
    void setUp() throws Exception {
        given(authInterceptor.preHandle(any(HttpServletRequest.class), any(HttpServletResponse.class), any(Object.class)))
                .willReturn(true);
    }

    @Test
    void 사용자의_프로젝트를_생성한_후_반환한다() throws Exception {
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
        User user = User.builder()
                .githubId("binimini")
                .name("subin")
                .avatar("github_content").build();
        String content = new ObjectMapper().writeValueAsString(createProjectRequest);

        given(userService.findUserById(managerId))
                .willReturn(user);
        given(projectService.createProject(eq(managerId), any(CreateProjectRequest.class)))
                .willReturn(project);

        // when
        ResultActions perform = mockMvc.perform(post("/users/" + managerId + "/projects")
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
    void 프로젝트_생성_시_필수_속성이_없을_경우_에러를_반환한다() throws Exception {
        // given
        CreateProjectRequest createProjectRequest = CreateProjectRequest.builder()
                .description(desc)
                .build();
        User user = User.builder()
                .githubId("binimini")
                .name("subin")
                .avatar("github_content").build();
        String content = new ObjectMapper().writeValueAsString(createProjectRequest);

        given(userService.findUserById(managerId))
                .willReturn(user);

        // when
        ResultActions perform = mockMvc.perform(post("/users/" + managerId + "/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("name : must not be blank"));
    }

    @Test
    void 프로젝트_생성_시_해당_사용자가_없을_경우_에러를_반환한다() throws Exception {
        // given
        CreateProjectRequest createProjectRequest = CreateProjectRequest.builder()
                .name(name)
                .description(desc)
                .build();
        String content = new ObjectMapper().writeValueAsString(createProjectRequest);

        given(userService.findUserById(managerId)).willThrow(new UserNotFoundException());

        // when
        ResultActions perform = mockMvc.perform(post("/users/" + managerId + "/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(new UserNotFoundException().getStatusCode().value()))
                .andExpect(jsonPath("$.message").value(new UserNotFoundException().getMessage()));
    }
}
