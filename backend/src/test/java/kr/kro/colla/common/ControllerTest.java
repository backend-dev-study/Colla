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
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected CookieManager cookieManager;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected ProjectService projectService;

    @MockBean
    protected UserProjectService userProjectService;

    @MockBean
    protected TaskService taskService;

    @MockBean
    protected StoryService storyService;

    @MockBean
    protected NoticeService noticeService;

    protected String accessToken = "token";
    protected LoginUser loginUser;

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
