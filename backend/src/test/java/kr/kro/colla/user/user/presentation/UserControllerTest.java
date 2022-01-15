package kr.kro.colla.user.user.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.common.fixture.FileProvider;
import kr.kro.colla.project.project.domain.Project;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.user.user.domain.User;
import kr.kro.colla.user.user.presentation.dto.CreateProjectRequest;
import kr.kro.colla.user.user.presentation.dto.UpdateUserNameRequest;
import kr.kro.colla.user.user.presentation.dto.UserProjectResponse;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user.user.service.dto.UserProjectResponseDto;
import kr.kro.colla.user_project.service.UserProjectService;
import kr.kro.colla.utils.CookieManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UserProjectService userProjectService;

    @MockBean
    private CookieManager cookieManager;

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
    void 사용자의_프로필을_조회한다() throws Exception {
        // given
        User user = User.builder()
                .name("kyk")
                .githubId("kykapple")
                .avatar("avatar.githubcontent")
                .build();

        given(userService.findUserById(loginUser.getId()))
                .willReturn(user);

        // when
        ResultActions perform = mockMvc.perform(get("/users/profile")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value(user.getName()))
                .andExpect(jsonPath("$.githubId").value(user.getGithubId()))
                .andExpect(jsonPath("$.avatar").value(user.getAvatar()));
    }

    @Test
    void 사용자_프로젝트_생성_후_반환한다() throws Exception {
        // given
        String name = "프로젝트 이름", desc = "프로젝트 설명";
        MockMultipartFile thumbnail = FileProvider.getTestMultipartFile("thumbnail.png");
        Project project = Project.builder()
                .managerId(loginUser.getId())
                .name(name)
                .description(desc)
                .build();
        User user = User.builder()
                .githubId("binimini")
                .name("subin")
                .avatar("github_content")
                .build();
        ReflectionTestUtils.setField(user, "id", loginUser.getId());

        given(userService.findUserById(loginUser.getId()))
                .willReturn(user);
        given(projectService.createProject(eq(loginUser.getId()), any(CreateProjectRequest.class)))
                .willReturn(project);

        ResultActions perform = mockMvc.perform(multipart("/users/projects")
                .file(thumbnail)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", name)
                .param("description", desc));
        // when

        // then
        perform.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.managerId").value(loginUser.getId()))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.description").value(desc));
        verify(projectService, times(1)).createProject(eq(loginUser.getId()), any(CreateProjectRequest.class));
    }

    @Test
    void 사용자_프로젝트_생성_실패_시_에러를_반환한다() throws Exception {
        // given
        String desc = "프로젝트 설명";
        MockMultipartFile thumbnail = FileProvider.getTestMultipartFile("thumbnail.png");

        // when
        ResultActions perform = mockMvc.perform(multipart("/users/projects")
                .file(thumbnail)
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("description", desc));

        // then
        perform
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("name : must not be blank"));
        verify(projectService, times(0)).createProject(any(), any());
    }

    @Test
    void 사용자의_이름을_변경한다() throws Exception {
        // given
        String newDisplayName = "new-name";
        UpdateUserNameRequest updateUserNameRequest = new UpdateUserNameRequest(newDisplayName);
        String content = objectMapper.writeValueAsString(updateUserNameRequest);

        given(userService.updateDisplayName(eq(loginUser.getId()), eq(newDisplayName)))
                .willReturn(newDisplayName);

        // when
        ResultActions perform = mockMvc.perform(patch("/users/name")
                .cookie(new Cookie("accessToken", accessToken))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        // then
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newDisplayName));
        verify(userService, times(1)).updateDisplayName(eq(loginUser.getId()), eq(newDisplayName));
    }

    @Test
    void 사용자가_진행중인_프로젝트_목록을_조회한다() throws Exception {
        // given
        Project project1 = Project.builder()
                .managerId(loginUser.getId())
                .name("project1")
                .description("project1 description")
                .build();
        Project project2 = Project.builder()
                .managerId(loginUser.getId())
                .name("project2")
                .description("project2 description")
                .build();
        List<UserProjectResponseDto> userProjectResponseDtoList = List.of(
                new UserProjectResponseDto(project1),
                new UserProjectResponseDto(project2)
        );

        given(userService.getUserProject(loginUser.getId()))
                .willReturn(userProjectResponseDtoList);

        // when
        ResultActions perform = mockMvc.perform(get("/users/projects")
                .cookie(new Cookie("accessToken", this.accessToken))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        MvcResult result = perform.andReturn();
        CollectionType collectionType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, UserProjectResponse.class);
        List<UserProjectResponse> userProjectResponseList = objectMapper.readValue(result.getResponse().getContentAsString(), collectionType);

        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name").value(containsInAnyOrder(project1.getName(), project2.getName())))
                .andExpect(jsonPath("$[*].description").value(containsInAnyOrder(project1.getDescription(), project2.getDescription())));
        assertThat(userProjectResponseList.size()).isEqualTo(2);
    }

}
