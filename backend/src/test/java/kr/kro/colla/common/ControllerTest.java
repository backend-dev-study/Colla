package kr.kro.colla.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.kro.colla.auth.domain.LoginUser;
import kr.kro.colla.auth.service.AuthService;
import kr.kro.colla.project.project.service.ProjectService;
import kr.kro.colla.story.service.StoryService;
import kr.kro.colla.task.task.service.TaskService;
import kr.kro.colla.user.notice.service.NoticeService;
import kr.kro.colla.user.user.service.UserService;
import kr.kro.colla.user_project.service.UserProjectService;
import kr.kro.colla.utils.CookieManager;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

public abstract class ControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @MockBean
    public CookieManager cookieManager;

    @MockBean
    public AuthService authService;

    @MockBean
    public UserService userService;

    @MockBean
    public ProjectService projectService;

    @MockBean
    public UserProjectService userProjectService;

    @MockBean
    public TaskService taskService;

    @MockBean
    public StoryService storyService;

    @MockBean
    public NoticeService noticeService;

    public String accessToken = "token";
    public LoginUser loginUser;

    @BeforeEach
    void setUp() {
        String accessToken = "token";
        loginUser = new LoginUser(20L);

        given(cookieManager.parseCookies(any(Cookie[].class), eq("accessToken")))
                .willReturn(new Cookie("accessToken", accessToken));
        given(authService.validateAccessToken(eq(accessToken)))
                .willReturn(true);
        given(authService.findUserFromToken(accessToken))
                .willReturn(loginUser);
    }

}
